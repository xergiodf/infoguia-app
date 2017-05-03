package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 4/10/17.
 */

public class GetSucursalesEvent {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetSucursalesEvent(String message){
        this.message = message;
    }
}
