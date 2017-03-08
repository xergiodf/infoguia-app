package py.minicubic.info_guia_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;

import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.event.ClienteServiceEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.NetworkStateChangeEvent;

/**
 * Created by hectorvillalba on 2/28/17.
 */

public class Subscriber extends Service implements MqttCallback {
    private boolean newServiceCreated;
    private static final String TAG = "Client";
    private Thread t;
    private MqttAsyncClient client;
    private Gson gson = new GsonBuilder().create();
    private static final String MQTT_TOPIC = "/ClienteServiceResponse/#";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    @Override
    public void onCreate() {
        Log.i(TAG, "new Service");
        EventBus.getDefault().register(this);
        newServiceCreated = true;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, TAG);
        t.start();
    }

    public synchronized void connect() {
        MemoryPersistence dataStore = new MemoryPersistence();
        try {

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(300);
            ///options.setUserName(getApplication().getString(R.string.user));
            ///options.setPassword(getApplication().getString(R.string.pass).toCharArray());
            String clientId = MqttAsyncClient.generateClientId();

            client = new MqttAsyncClient("tcp://45.79.159.123:1883",clientId, dataStore);

            Log.i("Client","Connected..."); //$NON-NLS-1$

            IMqttToken mqttToken = client.connect(options);
            mqttToken.waitForCompletion(10000);
            if (mqttToken.isComplete())
            {
                if (mqttToken.getException() != null)
                {
                    // TODO: retry
                }
            }
            client.setCallback(this);
            if(client.isConnected()){
                subscribe();
            }
        } catch (MqttException e) {
            e.printStackTrace();

            // TODO:  Log
            System.exit(1);
        }
    }

    public boolean subscribe() {
        int qos = 1;
        try {
            client.subscribe(MQTT_TOPIC, 2);
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

    public synchronized void disconnect() {

        try {
            EventBus.getDefault().unregister(this);
            if (client != null) {
                client.unsubscribe(MQTT_TOPIC);
                client.disconnect();

                Log.d(TAG, "Mqtt client disconnected");
                Log.i(TAG, "Servicio desconectado por cliente");
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "CONEXION PERDIDA "+ cause.getMessage());
        disconnect();
        connect();
    }



    @Override
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        if (topic.contains("ClienteServiceResponse")){
            Log.i(TAG, "llego respuesta de ClienteService: " + message.toString());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new ClienteServiceEvent(message.toString()));
                }
            });
            Log.i(TAG, message.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onNetWorkStateChangeEvent(NetworkStateChangeEvent event){
        if (event.isConnected()){
            if (!client.isConnected()){
                connect();
            }
        }else {
            disconnect();
        }

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void publicarMensaje(EventPublish eventPublish){
        try {
            Request request = eventPublish.getRequest();
            String message = gson.toJson(request);
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            if (request.getType().contains("ClienteService")){
                client.publish(request.getType(), mqttMessage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        disconnect();
        super.onDestroy();
    }
}
