package py.minicubic.info_guia_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.NetworkStateChangeEvent;
/**
 * Created by Minicubic on 2/11/17.
 */

public class Publicador extends Service{

    private boolean newServiceCreated;
    private static final String TAG = "Publicador";
    private Thread t;
    private MqttAsyncClient client;
    private Gson gson = new GsonBuilder().create();


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
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            Log.e(TAG, "Event bus error: " + e.getMessage());
        }
        MemoryPersistence dataStore = new MemoryPersistence();
        try {

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(getApplication().getString(R.string.user));
            options.setPassword(getApplication().getString(R.string.password).toCharArray());
            options.setCleanSession(false);
            String clientId = MqttAsyncClient.generateClientId();

            client = new MqttAsyncClient(getApplication().getString(R.string.host),clientId, dataStore);
            Log.i("Client","Connected..."); //$NON-NLS-1$

            IMqttToken mqttToken = client.connect(options);
            mqttToken.waitForCompletion(3000);
            if (mqttToken.isComplete()) {
                if (mqttToken.getException() != null)
                {
                    // TODO: retry
                }
            }
            if(client.isConnected()){
                Log.i(TAG, "PUBLICADOR CONECTADO");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public synchronized void disconnect() {

        try {
            EventBus.getDefault().unregister(this);
            if (client != null) {
                client.disconnect();

                Log.d(TAG, "Mqtt Client disconnected");
                Log.i(TAG, "Servicio desconectado por cliente");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void publicarMensaje(EventPublish eventPublish){
        try {
            Request request = eventPublish.getRequest();
            String message = gson.toJson(request);
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            if (request.getType().contains("Cliente")){
                client.publish(request.getType(), mqttMessage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

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
    @Override
    public void onDestroy() {
        disconnect();
        super.onDestroy();
    }
}
