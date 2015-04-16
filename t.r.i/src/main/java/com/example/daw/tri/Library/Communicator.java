package com.example.daw.tri.Library;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private JSONParser jsonParser;
    private static String URL = "http://www.vesely-pcpohotovost.cz/tri/pokus.php";

    public Communicator(){
        jsonParser = new JSONParser();
    }


    public JSONArray getTableDay(){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "getDay"));
        JSONArray json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONArray getTables(){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "getAll"));
        JSONArray json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
}
