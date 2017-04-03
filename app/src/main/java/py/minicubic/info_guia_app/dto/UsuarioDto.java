package py.minicubic.info_guia_app.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hectorvillalba on 3/25/17.
 */

public class UsuarioDto implements Serializable{

    private Long id;
    private String username;
    private String password;
    private Date fechaRegistro;
    private Date fechaExpiracion;
    private String tokenCambioPass;
    private String tokenConfirmacion;
    private ClienteDTO clienteDto;
    private TipoUsuarioDto tipoUsuarioDto;
    private EstadoUsuarioDto usuarioEstadoDto;
    private String tokenAuth;
    private String newPassword;
    private Boolean admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getTokenCambioPass() {
        return tokenCambioPass;
    }

    public void setTokenCambioPass(String tokenCambioPass) {
        this.tokenCambioPass = tokenCambioPass;
    }

    public String getTokenConfirmacion() {
        return tokenConfirmacion;
    }

    public void setTokenConfirmacion(String tokenConfirmacion) {
        this.tokenConfirmacion = tokenConfirmacion;
    }

    public ClienteDTO getClienteDTO() {
        return clienteDto;
    }

    public void setClienteDTO(ClienteDTO clienteDto) {
        this.clienteDto = clienteDto;
    }

    public TipoUsuarioDto getTipoUsuarioDto() {
        return tipoUsuarioDto;
    }

    public void setTipoUsuarioDto(TipoUsuarioDto tipoUsuarioDto) {
        this.tipoUsuarioDto = tipoUsuarioDto;
    }

    public EstadoUsuarioDto getUsuarioEstadoDto() {
        return usuarioEstadoDto;
    }

    public void setUsuarioEstadoDto(EstadoUsuarioDto usuarioEstadoDto) {
        this.usuarioEstadoDto = usuarioEstadoDto;
    }

    public String getTokenAuth() {
        return tokenAuth;
    }

    public void setTokenAuth(String tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
