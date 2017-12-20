package filip.superweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleForecast {
    private String forecastDate, forecastTime, mainWeather,
            mainWeatherDescription;
    private double temp, humidity, pressure, rain3h, snow3h, windSpeed, windDeg,
            cloudPercentage;
    private boolean isSnowy, isRainy, isWindy, isCloudy;
    private JSONObject forecastTopLevel;

    private static final String DT_ID = "dt";

    private static final String MAIN_ID = "main";
    private static final String DESCRIPTION_ID = "weather";
    private static final String CLOUDS_ID = "clouds";
    private static final String WIND_ID = "wind";
    private static final String RAIN_ID = "rain";
    private static final String SNOW_ID = "snow";

    private static final String TEMP_ID = "temp";
    private static final String HUMIDITY_ID = "humidity";
    private static final String PRESSURE_ID = "pressure";
    private static final String MAIN_WEATHER_ID = "main";
    private static final String MAIN_DESCRIPTION_ID = "description";
    private static final String ALL = "all";
    private static final String WIND_SPEED_ID = "speed";
    private static final String WIND_DEG_ID = "deg";
    private static final String H3 = "3h";

    private static final double KELVIN_ZERO = 273.15;


    /**
     * Creates a single forecast by fetching data from a JSONObject containing
     * the forecast information.
     *
     * @param forecastTopLevel	JSONObject containing the forecast.
     */
    SingleForecast(JSONObject forecastTopLevel) {

        this.forecastTopLevel = forecastTopLevel;

        parseWeather();

    }


    /**
     * Calls all the weather parsing methods.
     */
    private void parseWeather() {
        parseDateTime();
        parseMain();
        parseDescription();
        parseClouds();
        parseWind();
        parseRain();
        parseSnow();
    }


    /**
     * Converts an UNIX timestamp to readable date and time in the timezone
     * of the device. Returns the time on the format "yyyy-MM-dd HH:MM:ss z".
     *
     * @param unixInt   UNIX timestamp in seconds.
     * @return          String with timestamp.
     */
    String dateTimeFromUNIX(int unixInt) {
        Date date;
        SimpleDateFormat sdf;
        String dateStr;

        date = new Date(unixInt*1000L);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getDefault());
        dateStr = sdf.format(date);

        return dateStr;
    }


    /**
     * Fetches the date and the time.
     */
    private void parseDateTime() {
        String dateStr;
        String[] dateParts;

        try {
            dateStr = dateTimeFromUNIX(forecastTopLevel.getInt(DT_ID));

            dateParts = dateStr.split(" ");

            forecastDate = dateParts[0];
            forecastTime = dateParts[1];

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Fetches the temperature, humidity and air pressure.
     */
    private void parseMain() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(MAIN_ID);
            temp = obj.getDouble(TEMP_ID) - KELVIN_ZERO;
            humidity = obj.getDouble(HUMIDITY_ID);
            pressure = obj.getDouble(PRESSURE_ID);

        } catch (JSONException e) {
            temp = 0;
            humidity = 0;
            pressure = 0;
            e.printStackTrace();
        }

    }


    /**
     * Fetches the main weather string and the description.
     */
    private void parseDescription() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONArray(DESCRIPTION_ID).getJSONObject(0);
            mainWeather = obj.getString(MAIN_WEATHER_ID);
            mainWeatherDescription = obj.getString(MAIN_DESCRIPTION_ID);

        } catch (JSONException e) {
            mainWeather = "none";
            mainWeatherDescription = "none";
            e.printStackTrace();
        }

    }


    /**
     * Fetches the cloud percentage if available.
     */
    private void parseClouds() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(CLOUDS_ID);
            cloudPercentage = obj.getDouble(ALL);
            isCloudy = true;

        } catch (JSONException e) {
            cloudPercentage = 0;
            isCloudy = false;
        }
    }


    /**
     * Fetches the wind speed and the direction if available.
     */
    private void parseWind() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(WIND_ID);
            windSpeed = obj.getDouble(WIND_SPEED_ID);
            windDeg = obj.getDouble(WIND_DEG_ID);
            isWindy = true;

        } catch (JSONException e) {
            cloudPercentage = 0;
            isWindy = false;
        }
    }


    /**
     * Fetches the rain volume for the last three hours if available.
     */
    private void parseRain() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(RAIN_ID);
            rain3h = obj.getDouble(H3);
            isRainy = true;

        } catch (JSONException e) {
            rain3h = 0;
            isRainy = false;
        }
    }


    /**
     * Fetches the snow volume for the last three hours if available.
     */
    private void parseSnow() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(SNOW_ID);
            snow3h = obj.getDouble(H3);
            isSnowy = true;

        } catch (JSONException e) {
            snow3h = 0;
            isSnowy = false;
        }
    }


    @Override
    public String toString() {
        return getWeatherString();
    }


    /**
     * Returns a string with the weather information.
     *
     * @return	String.
     */
    String getWeatherString() {
        String str;

        str = "";

        str += String.format(
                "Date: %s\nTime: %s\n", forecastDate, forecastTime);

        str += String.format(
                "Weather: %s, %s\n", mainWeather, mainWeatherDescription);

        str += String.format(
                "Temperature: %.2f\u00b0C\nHumidity: %.0f%%\nPressure: %.0f hPa\n",
                temp, humidity, pressure);

        if (isWindy) {
            str += String.format(
                    "Wind speed: %.1f m/s, direction: %.1f\u00b0\n",
                    windSpeed, windDeg);
        }

        if (isSnowy) {
            str += String.format("Snow 3h: %.1f\n", snow3h);
        }

        if (isRainy) {
            str += String.format("Rain 3h: %.1f mm\n", rain3h);
        }

        if (isCloudy) {
            str += String.format("Clouds: %.1f%%\n", cloudPercentage);
        }

        return str;
    }

}

