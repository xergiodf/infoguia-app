package py.minicubic.info_guia_app.dto;

/**
 * Created by hectorvillalba on 5/8/17.
 */

public class CategoriaDTO {

    private Long id;
    private String descripcion;
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
