package oneForm;

import com.google.gson.annotations.SerializedName;
import td.api.Ticket;

import java.util.Map;

class Data {
    @SerializedName("ID")
    public int id;
}
public class responseBody {
    @SerializedName("metadata")
    public Data response;
}
