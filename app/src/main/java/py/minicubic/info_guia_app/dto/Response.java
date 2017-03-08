package py.minicubic.info_guia_app.dto;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class Response<T> {
    private Integer codigo;
    private String mensaje;
    private T data;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
