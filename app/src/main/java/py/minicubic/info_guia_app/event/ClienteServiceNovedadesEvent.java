package py.minicubic.info_guia_app.event;

/**
 * Created by Hector Villalba on 1/1/17.
 */

public class ClienteServiceNovedadesEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClienteServiceNovedadesEvent(String message){
        this.message = message;
    }
}
