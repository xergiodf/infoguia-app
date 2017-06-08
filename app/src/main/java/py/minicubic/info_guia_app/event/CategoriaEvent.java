package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 5/8/17.
 */

public class CategoriaEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoriaEvent(String message){
        this.message = message;
    }
}
