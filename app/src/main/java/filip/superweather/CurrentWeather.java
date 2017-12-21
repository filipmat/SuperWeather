package filip.superweather;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;

public class CurrentWeather extends SingleForecast {
    private String sunrise, sunset;
    private double visibility;

    private static final String SYS_ID = "sys";
    private static final String VISIBILITY_ID = "visibility";

    private static final String SUNRISE_ID = "sunrise";
    private static final String SUNSET_ID = "sunset";

    CurrentWeather(JSONObject currentTopLevel) {
        super(currentTopLevel);

        parseSunTimes(currentTopLevel);
        parseVisibility(currentTopLevel);

    }

    @Override
    public String toString() {
        String str;

        str = getWeatherString();

        str += String.format(Locale.UK,"Visibility: %.0f\n", visibility);

        str += String.format("Sunrise: %s\nSunset: %s\n", sunrise, sunset);


        return str;
    }


    /**
     * Fetches the sunrise and sunset hours.
     */
    private void parseSunTimes(JSONObject currentTopLevel) {
        JSONObject obj;
        String sunriseStr, sunsetStr;

        try {
            obj = currentTopLevel.getJSONObject(SYS_ID);

            sunriseStr = dateTimeFromUNIX(obj.getInt(SUNRISE_ID));
            sunsetStr = dateTimeFromUNIX(obj.getInt(SUNSET_ID));

            sunrise = sunriseStr.split(" ")[1];
            sunset = sunsetStr.split(" ")[1];

        } catch (JSONException e) {
            sunrise = "none";
            sunset = "none";
        }
    }


    /**
     * Fetches the visibility.
     */
    private void parseVisibility(JSONObject currentTopLevel) {
        try {
            visibility = currentTopLevel.getDouble(VISIBILITY_ID);
        } catch (JSONException e) {
            visibility = 0;
        }
    }


    /**
     * Returns the sunrise timestamp, formatted as HH:mm.
     *
     * @return  String.
     */
    String getSunrise() {
        return sunrise;
    }


    /**
     * Returns the sunset timestamp, formatted as HH:mm.
     *
     * @return  String.
     */
    String getSunset() {
        return sunset;
    }

}

