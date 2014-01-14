package io.thera.zen.json;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 14/01/14.
 */
public class ZenJsonCast {

    public static String[] toArray(JSONArray jarr) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < jarr.length(); i++){
            list.add(jarr.getString(i));
        }
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        return arr;
    }

}
