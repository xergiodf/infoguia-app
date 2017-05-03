package py.minicubic.info_guia_app.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hectorvillalba on 3/20/17.
 */

public class PublicacionClienteDTO implements Serializable {

    private Long id;
    private Long id_cliente;
    private Long tipo_publicaciones_id;
    private Long id_estado_publicacion;
    private String titulo;
    private String descripcion_corta;
    private String dir_imagen;
    private String boton_accion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }
    public Long getTipo_publicaciones_id() {
        return tipo_publicaciones_id;
    }

    public void setTipo_publicaciones_id(Long tipo_publicaciones_id) {
        this.tipo_publicaciones_id = tipo_publicaciones_id;
    }

    public Long getId_estado_publicacion() {
        return id_estado_publicacion;
    }

    public void setId_estado_publicacion(Long id_estado_publicacion) {
        this.id_estado_publicacion = id_estado_publicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion_corta() {
        return descripcion_corta;
    }

    public void setDescripcion_corta(String descripcion_corta) {
        this.descripcion_corta = descripcion_corta;
    }

    public String getDir_imagen() {
        return dir_imagen;
    }

    public void setDir_imagen(String dir_imagen) {
        this.dir_imagen = dir_imagen;
    }

    public String getBoton_accion() {
        return boton_accion;
    }

    public void setBoton_accion(String boton_accion) {
        this.boton_accion = boton_accion;
    }
}
