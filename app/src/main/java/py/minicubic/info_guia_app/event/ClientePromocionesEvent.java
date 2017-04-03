package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 3/28/17.
 */

public class ClientePromocionesEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientePromocionesEvent(String message){
        this.message = message;
    }
}
