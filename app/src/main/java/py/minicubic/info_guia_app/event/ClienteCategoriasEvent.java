package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 5/8/17.
 */

public class ClienteCategoriasEvent {
    private String message;

    public ClienteCategoriasEvent (String message){
        this.message = message;

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
