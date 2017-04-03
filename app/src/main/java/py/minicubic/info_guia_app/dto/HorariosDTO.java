package py.minicubic.info_guia_app.dto;

/**
 * Created by hectorvillalba on 3/19/17.
 */

public class HorariosDTO {

    private Long id;
    private String dias;
    private String hora_desde;
    private String hora_hasta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHora_desde() {
        return hora_desde;
    }

    public void setHora_desde(String hora_desde) {
        this.hora_desde = hora_desde;
    }

    public String getHora_hasta() {
        return hora_hasta;
    }

    public void setHora_hasta(String hora_hasta) {
        this.hora_hasta = hora_hasta;
    }
}
