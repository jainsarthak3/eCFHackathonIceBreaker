import com.google.gson.JsonElement;

public class StandardResponse {
    private String status;
    private String message;
    private JsonElement data;

    public StandardResponse(String status, JsonElement data) {
        this.status = status;
        this.data = data;
        this.message = "";
    }

    public StandardResponse(String status, String message, JsonElement data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
