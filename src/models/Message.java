package models;

import com.sun.istack.internal.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public class Message {

    public static final int MOVE_REQUEST = 45627;
    public static final int REFRESH_REQUEST = 213123;
    public static final String KEY_LOCATION = "KEYLOCATION";
    private Map<String, Object> parameters;
    private int requestType;

    public Message(@Nullable Map<String, Object> parameters, int requestType) {
        this.parameters = parameters;
        this.requestType = requestType;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public int getRequestType() {
        return requestType;
    }


    @Retention(RetentionPolicy.RUNTIME)
    public @interface Requests {

    }
}
