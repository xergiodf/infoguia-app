package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 5/4/17.
 */

public class CargarPublicacionPrincipalEvent {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CargarPublicacionPrincipalEvent(String message){
        this.message = message;
    }
}
