package py.minicubic.info_guia_app.dto;

import java.io.Serializable;

/**
 * Created by hectorvillalba on 3/25/17.
 */

public class TipoUsuarioDto implements Serializable {
    private Integer id;
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
