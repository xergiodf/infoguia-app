package py.minicubic.info_guia_app.event;


import py.minicubic.info_guia_app.dto.Request;

/**
 * Created by Hector Villalba on 1/4/17.
 * Clase que se encarga de publicar al broker
 */

public class EventPublish {
    private Request request;

    public Request getRequest() {
        return request;
    }

    public EventPublish(Request request){
        this.request = request;
    }

}
