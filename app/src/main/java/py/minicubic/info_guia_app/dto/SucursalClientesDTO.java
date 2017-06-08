package py.minicubic.info_guia_app.dto;

import java.io.Serializable;

/**
 * Created by hectorvillalba on 3/24/17.
 */

public class SucursalClientesDTO implements Serializable{

    private Long id;
    private String nombre_sucursal;
    private String direccion_fisica;
    private String coordenadas;
    private Long id_cliente;
    private String horarios;
    private String telefonos;
    private String telefono2;
    private String telefono3;
    private Double lat;
    private Double lon;
    private String duracion;
    private String distancia;
    private String photo_url;

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public String getDireccion_fisica() {
        return direccion_fisica;
    }

    public void setDireccion_fisica(String direccion_fisica) {
        this.direccion_fisica = direccion_fisica;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefono) {
        this.telefonos = telefono;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTelefono3() {
        return telefono3;
    }

    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
