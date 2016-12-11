package unidev.uniteam;

import org.json.JSONArray;

class DatabaseGetResult {

    private String returnedObject;
    private JSONArray data;

    String getReturnedType() {
        return returnedObject;
    }

    public JSONArray getData() {
        return data;
    }

    DatabaseGetResult(String returnedObject, JSONArray data) {
        this.returnedObject = returnedObject;
        this.data = data;
    }

}
