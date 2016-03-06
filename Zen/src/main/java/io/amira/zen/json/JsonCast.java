/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.json;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonCast {

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
