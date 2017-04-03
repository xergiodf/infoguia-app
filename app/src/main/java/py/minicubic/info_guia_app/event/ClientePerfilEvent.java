package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 3/16/17.
 */

public class ClientePerfilEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientePerfilEvent(String message){
        this.message = message;
    }
}
