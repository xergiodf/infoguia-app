package py.minicubic.info_guia_app.service;

/**
 * Created by hectorvillalba on 5/25/17.
 */

        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.location.Location;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.Looper;
        import android.telephony.TelephonyManager;
        import android.util.Log;

        import com.facebook.internal.Logger;
        import com.facebook.login.LoginManager;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;

        import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
        import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
        import org.eclipse.paho.client.mqttv3.MqttCallback;
        import org.eclipse.paho.client.mqttv3.MqttClient;
        import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
        import org.eclipse.paho.client.mqttv3.MqttException;
        import org.eclipse.paho.client.mqttv3.MqttMessage;
        import org.eclipse.paho.client.mqttv3.MqttSecurityException;
        import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
        import org.greenrobot.eventbus.EventBus;
        import org.greenrobot.eventbus.Subscribe;
        import org.greenrobot.eventbus.ThreadMode;

        import py.minicubic.info_guia_app.R;
        import py.minicubic.info_guia_app.event.CargarPublicacionPrincipalEvent;
        import py.minicubic.info_guia_app.event.CargarPublicacionSecundariaEvent;
        import py.minicubic.info_guia_app.event.CategoriaEvent;
        import py.minicubic.info_guia_app.event.ClienteCategoriasEvent;
        import py.minicubic.info_guia_app.event.ClientePerfilEvent;
        import py.minicubic.info_guia_app.event.ClientePromocionesEvent;
        import py.minicubic.info_guia_app.event.ClienteServiceNovedadesEvent;
        import py.minicubic.info_guia_app.event.GetSucursalesEvent;
        import py.minicubic.info_guia_app.event.ListaClientesEvent;
        import py.minicubic.info_guia_app.event.NetworkStateChangeEvent;
        import py.minicubic.info_guia_app.event.SubscriberAlready;
        import py.minicubic.info_guia_app.util.CacheData;

/**
 * Created by nbd on 1/26/15.
 */
public class Service2 extends Service implements MqttCallback {
    private static final String TAG = "Subscriber";
    private static final long CONN_RETRY_DELAY_IN_MILLIS = 1*10000;
    private Handler handler;
    private MqttClient client;
    private Context _context;
    private Thread t;
    private boolean newServiceCreated;
    private CacheData cacheData = CacheData.getInstance();
    private Gson gson = new GsonBuilder().create();
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {

        Log.i(TAG, "new Service");
        newServiceCreated = true;

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                handleStart();
            }
        }, TAG);

        t.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {

        boolean newService = newServiceCreated;
        newServiceCreated = false;

        if (newService) {
            Log.i(TAG, "onStartCommand: Start already handled");
            return START_STICKY;
        }


        if (client == null) {

            Log.i(TAG, "onStartCommand: Client not instantiated. Connecting...");
            connect();

        } else {

            Log.i(TAG, "onStartCommand: Client already connected. Disconnecting first...");

            // Hacemos esto en un nuevo thread para evitar el bloqueo de pantalla si la conexión está lenta o el broker tarda en responder. (pantalla negra)
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    disconnect();
                    connect();
                }
            }, TAG);

            t.start();
        }

        return START_STICKY;
    }

    synchronized void handleStart() {

        //_context = getApplicationContext();
        Log.i(TAG,  "Subscriber service started on new thread");


        //Aquí se producía esta excepción: java.lang.RuntimeException: Unable to start service
        // Subscriber@40e1f748 with
        // Intent { flg=0x4 cmp=py.com.itapua.android.mobank/.service.Subscriber (has extras) }:
        // de.greenrobot.event.EventBusException: Subscriber class Subscriber
        // already registered to event class NetworkStateChangeEvt
        try {
           EventBus.getDefault().register(this);

        } catch (Exception e) {
            Log.e(TAG, "Event bus error: " + e.getMessage());
        }
        connect();
    }

    public synchronized void connect() {
        MemoryPersistence dataStore = new MemoryPersistence();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(getApplication().getString(R.string.user));
        options.setPassword(getApplication().getString(R.string.password).toCharArray());
        options.setCleanSession(false);
        options.setKeepAliveInterval(300);
        String clientId = MqttAsyncClient.generateClientId();

        try {
            if (client == null) {
                Log.i(TAG, "MQTTClient == null. Instanciando...");
            }

            client = new MqttClient(getApplication().getString(R.string.host),clientId, dataStore);
        } catch (MqttException e) {
            e.printStackTrace();
        }




        try {

            client.setCallback(this);
            client.connect(options);

           subscribe();
            EventBus.getDefault().post(new SubscriberAlready("iniciado"));
        } catch (MqttSecurityException ex) {
            if (ex.getReasonCode() == MqttException.REASON_CODE_FAILED_AUTHENTICATION) {
                Log.e(TAG, "REASON_CODE_FAILED_AUTHENTICATION: " + ex.getMessage());
            } else {
                handler.postDelayed(connectionRetry, CONN_RETRY_DELAY_IN_MILLIS);
            }

        } catch (MqttException e) {
            //Logger.log(_context, TAG, Log.DEBUG, "Mqtt exception: " + e.getMessage() + ". Causa: " + e.getCause().getMessage());

            if (e.getReasonCode() == MqttException.REASON_CODE_FAILED_AUTHENTICATION) {
                Log.e(TAG, "REASON_CODE_FAILED_AUTHENTICATION: " + e.getMessage());
            } else {
                handler.postDelayed(connectionRetry, CONN_RETRY_DELAY_IN_MILLIS);
            }
        } catch (NullPointerException ee) {
            Log.e(TAG, "NullPointerException: " + ee.getMessage());
        }
    }

    public synchronized void disconnect() {

        try {

            EventBus.getDefault().unregister(this);
            if (client != null) {
                client.unsubscribe(getApplication().getString(R.string.topic) + cacheData.getImei() + "/#");
                client.disconnect();

                Log.d(TAG, "Mqtt client disconnected");
                Log.i(TAG, "Servicio desconectado por cliente");
            }

        } catch (MqttException e) {
            Log.e(TAG, "Error al intentar desconectarse del MQTT. Motivo: " + e.getMessage());

            if (e.getCause() != null) {
                Log.e(TAG, "Causa: " + e.getCause().getMessage());
            }
        }
    }

    public boolean subscribe() {
        int qos = 1;
        try {
            client.subscribe(getApplication().getString(R.string.topic) + cacheData.getImei() + "/#", 2);
            return true;

        } catch (MqttException e) {
            Log.e(TAG, null, e);
            Log.i("Error Mqtt","No se pudo subscribir ");
        } catch (NullPointerException e){
            Log.e(TAG, null, e);
            Log.i("Error Mqtt","No se pudo subscribir ");
        }
        return false;
    }

    public Runnable connectionRetry = new Runnable() {
        @Override
        public void run() {

            Service2.this.reconnect();

        }
    };

    @Override
    public void onDestroy() {

        if(t.isAlive()) {
            t.interrupt();
        }

        handler.removeCallbacks(connectionRetry);

        if (client != null && client.isConnected()) {
            disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        String topicos[] = topic.split("/");
        final String pantallaId = topicos[5];
        Log.i(TAG, "llego respuesta de ClienteService: " + message.toString());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (pantallaId.equalsIgnoreCase("novedades")){
                    EventBus.getDefault().post(new ClienteServiceNovedadesEvent(message.toString()));
                }else if (pantallaId.equalsIgnoreCase("promociones")){
                    EventBus.getDefault().post(new ClientePromocionesEvent(message.toString()));
                }else if(pantallaId.equalsIgnoreCase("clienteperfil")){
                    EventBus.getDefault().post(new ClientePerfilEvent(message.toString()));
                }else if(pantallaId.equalsIgnoreCase("clientemain")){
                    EventBus.getDefault().post(new ListaClientesEvent(message.toString()));
                }else if(pantallaId.equalsIgnoreCase("sucursales")){
                    EventBus.getDefault().post(new GetSucursalesEvent(message.toString()));
                }else if(pantallaId.equalsIgnoreCase("publicaciones")){
                    EventBus.getDefault().post(new CargarPublicacionPrincipalEvent(message.toString()));
                }else if (pantallaId.equalsIgnoreCase("categoria")){
                    EventBus.getDefault().post(new CategoriaEvent(message.toString()));
                }else if (pantallaId.equalsIgnoreCase("clientecategorias")){
                    EventBus.getDefault().post(new ClienteCategoriasEvent(message.toString()));
                }else if(pantallaId.equalsIgnoreCase("publicacionessecundarias")){
                    EventBus.getDefault().post(new CargarPublicacionSecundariaEvent(message.toString()));
                }
            }
        });
        Log.i(TAG, message.toString());
    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d(TAG, "Connection lost!");
        Log.d(TAG, "localized message: " + throwable.getLocalizedMessage());
        Log.d(TAG, "cause: " + throwable.getCause().toString());
        Log.d(TAG, "exception message: " + throwable.getMessage());
        //Logger.lostConection("Servicio perdio conexion");


        ConnectivityManager connMgr = (ConnectivityManager) Service2.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            Log.e(TAG, "Device online. Trying to reconnect");
            handler.postDelayed(connectionRetry, CONN_RETRY_DELAY_IN_MILLIS);
        }
    }



    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onNetWorkStateChangeEvent(NetworkStateChangeEvent event){
        Log.d(TAG, "Netowork changed...");
        if (!event.isConnected()) {

            //notifyConnectChange(CellUtil.SUSCRIBER_CONNECTION_OFF);
            mqttClientDisconnect();

            Log.e(TAG,  "Device offline");
            return;
        }

        Service2.this.reconnect();
    }
    private void mqttClientDisconnect() {

        try {
            Log.d(TAG, "Connection lost!");

            if (client != null && !client.isConnected()) {
                client.disconnect();
                Log.d(TAG, client.isConnected() ? "is connected" : "NOT connected");
            }

        } catch (MqttException ex) {
            Log.d(TAG, "DISCONNECT EXCEPTION: " + ex);
        }
    }

    private void reconnect() {

        if (client == null || !client.isConnected()) {
            Log.e(TAG, "Client not connected. Trying to reconnect...");
            disconnect();
            connect();
        }else if (client.isConnected()){
            Log.e(TAG, "Client reconnected. Trying to reconnect...");
        }
    }






}
