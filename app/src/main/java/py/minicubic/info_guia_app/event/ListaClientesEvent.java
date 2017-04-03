package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 3/27/17.
 */

public class ListaClientesEvent {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ListaClientesEvent(String message){
        this.message = message;
    }
}
