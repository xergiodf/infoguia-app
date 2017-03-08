package py.minicubic.info_guia_app.model;

import java.util.List;

/**
 * Created by hectorvillalba on 2/4/17.
 */

public class Novedades {
    private Long id;
    private String descripcion;
    private String titulo;
    private List<Integer> imagenes;

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Integer> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Integer> imagenes) {
        this.imagenes = imagenes;
    }
}
