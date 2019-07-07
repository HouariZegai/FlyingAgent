package models.information;

import com.google.gson.Gson;

import java.io.Serializable;

class Information implements Serializable {

    public final String toJson() {
        return new Gson().toJson(this);
    }

}
