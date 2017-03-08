package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 1/26/17.
 */

public class NetworkStateChangeEvent {

    private boolean connected;

    public NetworkStateChangeEvent(boolean connected){
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
