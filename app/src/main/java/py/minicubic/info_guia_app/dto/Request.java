package py.minicubic.info_guia_app.dto;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class Request<T> {
    private String type;
    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
