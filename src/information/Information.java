package information;

import com.google.gson.Gson;

class Information {

    public final String toJson() {
        return new Gson().toJson(this);
    }

}
