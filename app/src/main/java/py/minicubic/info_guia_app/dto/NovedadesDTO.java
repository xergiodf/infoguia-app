package py.minicubic.info_guia_app.dto;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class NovedadesDTO {

    private Integer id;
    private String titulo;
    private String descripconCorta;
    private String dirImagen;
    private Integer idCliente;
    private String nombreCompleto;
    private String tipoPublicacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripconCorta() {
        return descripconCorta;
    }

    public void setDescripconCorta(String descripconCorta) {
        this.descripconCorta = descripconCorta;
    }

    public String getDirImagen() {
        return dirImagen;
    }

    public void setDirImagen(String dirImagen) {
        this.dirImagen = dirImagen;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }
}
