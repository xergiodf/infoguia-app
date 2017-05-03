package py.minicubic.info_guia_app.event;

/**
 * Created by hectorvillalba on 4/26/17.
 */

public class ObtenerDistanciaEvent {

    private String distancia;
    private String duracion;
    private Double value;

    public ObtenerDistanciaEvent(String distancia, String duracion, Double value) {
        this.distancia = distancia;
        this.duracion = duracion;
        this.value = value;
    }

    public String getDistancia() {
        return distancia;
    }

    public String getDuracion() {
        return duracion;
    }

    public Double getValue() {
        return value;
    }
}
