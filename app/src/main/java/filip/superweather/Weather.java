package filip.superweather;

import java.net.*;
import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private List<SingleForecast> forecasts;
    private CurrentWeather current;
    private JSONObject forecastTopLevel, currentTopLevel;
    private String locationName, countryCode;

    private static final String SRV = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "cfb479cde26c179e7443b3942057de84";
    private static final String FORECAST = "forecast";
    private static final String WEATHER = "weather";

    private static final String LIST_ID = "list";
    private static final String CITY_ID = "city";
    private static final String NAME_ID = "name";
    private static final String COUNTRY_ID = "country";

    Weather(String cityID) {

        gatherCurrentData(cityID);
        gatherForecastData(cityID);

        forecasts = getForecasts();
        current = new CurrentWeather(currentTopLevel);

        parseLocation();
    }


    /**
     * Returns a URL for the OpenWeatherMap API call.
     *
     * @param cityID	ID of the city.
     * @param what		Which type of data is concerned, e.g. "weather".
     * @return			Complete URL string.
     */
    private String getWeatherURL(String cityID, String what) {
        return SRV + what + "?id=" + cityID + "&appid=" + API_KEY;
    }


    /**
     * Structures weather forecast. From forecastTopLevel a SingleForecast is
     * created for each element in the array "list". All objects are put into
     * an ArrayList.
     *
     * Should be called after gatherForecastData() has been called.
     *
     * @return	ArrayList with SingleForecast objects.
     */
    private List<SingleForecast> getForecasts() {
        List<SingleForecast> list;
        JSONArray array;

        list = new ArrayList<>();

        try {
            array = forecastTopLevel.getJSONArray(LIST_ID);

            for (int i = 0; i < array.length(); i++) {
                list.add(new SingleForecast(array.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * Fetches the location name and country code.
     */
    private void parseLocation() {
        JSONObject obj;

        try {
            obj = forecastTopLevel.getJSONObject(CITY_ID);
            locationName = obj.getString(NAME_ID);
            countryCode = obj.getString(COUNTRY_ID);

        } catch (JSONException e) {
            locationName = "none";
            countryCode = "XX";
            e.printStackTrace();
        }
    }


    /**
     * Gathers the current weather data. The data is put in the JSONObject
     * currentTopLevel.
     *
     * @param cityID	ID of the city.
     */
    private void gatherCurrentData(String cityID) {
        String url;

        url = getWeatherURL(cityID, WEATHER);

        currentTopLevel = gatherData(url);

    }


    /**
     * Gathers the forecast data. The data is put in the JSONObject
     * forecastTopLevel.
     *
     * @param cityID	ID of the city.
     */
    private void gatherForecastData(String cityID) {
        String url;

        url = getWeatherURL(cityID, FORECAST);

        forecastTopLevel = gatherData(url);

    }


    /**
     * Gathers the raw weather data with the address url. Returns a
     * JSONObject created on the data.
     *
     * @param url	URL for API call.
     * @return		JSONObject.
     */
    private JSONObject gatherData(String url) {
        String rawData;
        JSONObject obj;

        rawData = getRawData(url);

        try {
            obj = new JSONObject(rawData);

        } catch (JSONException e) {
            obj = new JSONObject();
            e.printStackTrace();
        }

        return obj;
    }


    /**
     * Calls the OpenWeatherMap server and gets the weather information.
     *
     * @param urlstr    String containing the full url address.
     * @return       	String containing all the information.
     */
    private String getRawData(String urlstr) {
        String response, input;
        URL url;
        InputStream stream;
        HttpURLConnection urlCon;
        BufferedReader br;
        StringBuilder builder;

        try {
            url = new URL(urlstr);

            urlCon = (HttpURLConnection) url.openConnection();

            stream = new BufferedInputStream(urlCon.getInputStream());

            br = new BufferedReader(new InputStreamReader(stream));

            builder = new StringBuilder();

            while ((input = br.readLine()) != null) {
                builder.append(input);
            }

            response = builder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            response = "{}";
        }

        return response;

    }


    /**
     * Returns the sunrise timestamp.
     *
     * @return  String.
     */
    public String getSunrise() {
        return current.getSunrise();
    }


    /**
     * Returns the sunset timestamp.
     *
     * @return  String.
     */
    public String getSunset() {
        return current.getSunset();
    }


    /**
     * Returns a string with the current weather and the location.
     *
     * @return  String.
     */
    String getCurrentWeatherString() {
        String str;

        str = String.format("%s, %s\n", locationName, countryCode);

        str += current.toString();

        return str;
    }


    @Override
    public String toString() {
        return getCurrentWeatherString();
    }

}

