package unidev.uniteam;

import org.json.JSONArray;

public class DatabaseGetResult {

    private String returnedObject;
    private JSONArray data;

    public String getReturnedType() {
        return returnedObject;
    }

    public JSONArray getData() {
        return data;
    }

    public DatabaseGetResult(String returnedObject, JSONArray data) {
        this.returnedObject = returnedObject;
        this.data = data;
    }

}
