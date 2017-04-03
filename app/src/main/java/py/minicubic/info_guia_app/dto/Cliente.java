package py.minicubic.info_guia_app.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by hectorvillalba on 3/16/17.
 */

public class Cliente implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String nombreCompleto;
    private String nombreCorto;
    private String descriptionCompleta;
    private String descripcionCorta;
    private Date fechaAlta;
    private Date fechaInicio;

    public Cliente() {
    }

    public Cliente(Long id) {
        this.id = id;
    }

    public Cliente(Long id, String nombreCompleto, Date fechaAlta) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.fechaAlta = fechaAlta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getDescriptionCompleta() {
        return descriptionCompleta;
    }

    public void setDescriptionCompleta(String descriptionCompleta) {
        this.descriptionCompleta = descriptionCompleta;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.minicubic.info_guia_app.Cliente[ id=" + id + " ]";
    }

}
