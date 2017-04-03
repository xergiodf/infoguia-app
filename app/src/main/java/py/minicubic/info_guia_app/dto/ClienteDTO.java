package py.minicubic.info_guia_app.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class ClienteDTO implements Serializable{

    private Long id;
    private String nombre_completo;
    private String nombre_corto;
    private String descripcion_completa;
    private String descripcion_corta;
    private String fecha_alta;
    private String fecha_inicio;
    private String nombre_sucursal;
    private String direccion_fisica;
    private String coordenadas;
    private String telefono;
    private List<HorariosDTO> horarios;
    private List<SucursalClientesDTO> sucursalClientes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getNombre_corto() {
        return nombre_corto;
    }

    public void setNombre_corto(String nombre_corto) {
        this.nombre_corto = nombre_corto;
    }

    public String getDescripcion_completa() {
        return descripcion_completa;
    }

    public void setDescripcion_completa(String descripcion_completa) {
        this.descripcion_completa = descripcion_completa;
    }

    public String getDescripcion_corta() {
        return descripcion_corta;
    }

    public void setDescripcion_corta(String descripcion_corta) {
        this.descripcion_corta = descripcion_corta;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<HorariosDTO> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorariosDTO> horarios) {
        this.horarios = horarios;
    }

    public List<SucursalClientesDTO> getSucursalClientes() {
        return sucursalClientes;
    }

    public void setSucursalClientes(List<SucursalClientesDTO> sucursalClientes) {
        this.sucursalClientes = sucursalClientes;
    }
}