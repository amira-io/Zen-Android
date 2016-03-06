/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.geo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.util.Log;

import io.amira.zen.json.ZenJson;

public class ZenGeoDirections {
    public enum TravelMode {
        BIKING("biking"),
        DRIVING("driving"),
        WALKING("walking"),
        TRANSIT("transit");

        protected String _sValue;

        private TravelMode(String sValue) {
            this._sValue = sValue;
        }

        protected String getValue() { return _sValue; }
    }

    protected TravelMode travelMode;
    private int distance;
    private ZenRoute route;

    private String method;
    private Object caller;

    public ZenGeoDirections(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public void getData(LatLng start, LatLng end, String method, Object caller) {
        this.method = method;
        this.caller = caller;

        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode="+travelMode.getValue();
        Log.d("url", url);

        //ZenJsonManager.parseJson(url, "parseData", this);
        ZenJson.get(url, this, "parseData", "parseData");

    }

    /**
     * Parses a url pointing to a Google JSON object to a Route object.
     * @return a Route object based on the JSON object by Haseem Saheed
     */

    public void parseData(JSONObject json) {
        // turn the stream into a string
        //final String result = convertStreamToString(this.getInputStream());
        //if (result == null) return null;

        //Create an empty route
        final ZenRoute route = new ZenRoute();
        //Create an empty segment
        final ZenSegment segment = new ZenSegment();
        try {
            //Get the route object
            final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
            //Get the leg, only one leg as we don't support waypoints
            final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
            //Get the steps for this leg
            final JSONArray steps = leg.getJSONArray("steps");
            //Number of steps for use in for loop
            final int numSteps = steps.length();
            //Set the name of this route using the start & end addresses
            route.setName(leg.getString("start_address") + " to " + leg.getString("end_address"));
            //Get google's copyright notice (tos requirement)
            route.setCopyright(jsonRoute.getString("copyrights"));
            //Get the total length of the route.
            route.setLength(leg.getJSONObject("distance").getInt("value"));
            //Get any warnings provided (tos requirement)
            if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
                route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
            }
                    /* Loop through the steps, creating a segment for each one and
                     * decoding any polylines found as we go to add to the route object's
                     * map array. Using an explicit for loop because it is faster!
                     */
            for (int i = 0; i < numSteps; i++) {
                //Get the individual step
                final JSONObject step = steps.getJSONObject(i);
                //Get the start position for this step and set it on the segment
                final JSONObject start = step.getJSONObject("start_location");
                final LatLng position = new LatLng(start.getDouble("lat"),
                        start.getDouble("lng"));
                segment.setPoint(position);
                //Set the length of this segment in metres
                final int length = step.getJSONObject("distance").getInt("value");
                distance += length;
                segment.setLength(length);
                segment.setDistance(distance/1000);
                //Strip html from google directions and set as turn instruction
                segment.setInstruction(step.getString("html_instructions").replaceAll("<(.*?)*>", ""));
                //Retrieve & decode this segment's polyline and add it to the route.
                route.addPoints(decodePolyLine(step.getJSONObject("polyline").getString("points")));
                //Push a copy of the segment to the route
                route.addSegment(segment.copy());
            }
            this.route = route;
        } catch (JSONException e) {
            Log.e("Routing Error",e.getMessage());
            this.route = null;
        }
        //return route;
        postExecute();
    }

    /**
     * Decode a polyline string into a list of GeoPoints.
     * @param poly polyline encoded string to decode.
     * @return the list of GeoPoints represented by this polystring.
     */

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat/100000d, lng/100000d
            ));
        }

        return decoded;
    }

    protected void postExecute() {
        try {
            Class[] params = new Class[1];
            params[0] = PolylineOptions.class;
            //params[0] = ArrayList.class;
            Object[] values = new Object[1];
            if (route != null) {
                PolylineOptions res = new PolylineOptions();
                for (LatLng point : route.getPoints()) {
                    res.add(point);
                }
                values[0] = res;
            }
            else {
                values[0] = null;
            }
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.method, params).invoke(this.caller,values);
            }
            else {
                this.caller.getClass().getMethod(this.method, params).invoke(this.caller, values);
            }
        }
        catch (Exception e) {
            return;
        }
    }
}
