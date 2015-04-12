package com.example.daw.tri;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private JSONParser jsonParser;
    private static String URL = "http://vesely-pcpohotovost.cz/tri/pokus.php";

    public Communicator(){
        jsonParser = new JSONParser();
    }


    public JSONObject getTableDay(){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "getDay"));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
}
