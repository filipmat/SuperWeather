package filip.superweather;

import android.content.Context;

import java.net.*;
import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather implements Serializable {
    private List<SingleForecast> forecasts;
    private List<DayWeather> dayWeathers;
    private CurrentWeather current;
    private String locationName, countryCode, apiKey;

    private static final String SRV = "http://api.openweathermap.org/data/2.5/";
    private static final String FORECAST = "forecast";
    private static final String WEATHER = "weather";

    private static final String LIST_ID = "list";
    private static final String CITY_ID = "city";
    private static final String NAME_ID = "name";
    private static final String COUNTRY_ID = "country";

    Weather(String cityID, String apiKey) {
        JSONObject fTopLevel, cTopLevel;

        this.apiKey = apiKey;

        cTopLevel = gatherCurrentData(cityID);
        fTopLevel = gatherForecastData(cityID);

        forecasts = getForecasts(fTopLevel);
        current = new CurrentWeather(cTopLevel);

        dayWeathers = getDayWeathers();

        parseLocation(fTopLevel);
    }


    /**
     * Returns a URL for the OpenWeatherMap API call.
     *
     * @param cityID	ID of the city.
     * @param what		Which type of data is concerned, e.g. "weather".
     * @return			Complete URL string.
     */
    private String getWeatherURL(String cityID, String what) {

        return SRV + what + "?id=" + cityID + "&appid=" + apiKey;
    }


    /**
     * Creates DayWeather objects from the 5 day/3 h forecast. All forecasts
     * from the same date are passed to the same DayWeather object.
     *
     * @return  ArrayList with the DayWeather objects.
     */
    private List<DayWeather> getDayWeathers() {
        List<DayWeather> dayWs;
        String lastDate, fcDate;
        int count;

        count = -1;
        lastDate = "";
        dayWs = new ArrayList<>();

        for (int i = 0; i < forecasts.size(); i++) {
            fcDate = forecasts.get(i).getForecastDate();

            if (!fcDate.equals(lastDate)) {
                count++;
                dayWs.add(new DayWeather(fcDate));
                lastDate = fcDate;
            }

            dayWs.get(count).addForecast(forecasts.get(i));
        }

        return dayWs;

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
    private List<SingleForecast> getForecasts(JSONObject forecastTopLevel) {
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
    private void parseLocation(JSONObject forecastTopLevel) {
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
    private JSONObject gatherCurrentData(String cityID) {
        String url;
        JSONObject cTopLevel;

        url = getWeatherURL(cityID, WEATHER);

        cTopLevel = gatherData(url);

        return cTopLevel;

    }


    /**
     * Gathers the forecast data. The data is put in the JSONObject
     * forecastTopLevel.
     *
     * @param cityID	ID of the city.
     */
    private JSONObject gatherForecastData(String cityID) {
        String url;
        JSONObject fTopLevel;

        url = getWeatherURL(cityID, FORECAST);

        fTopLevel = gatherData(url);

        return fTopLevel;

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
    String getSunrise() {
        return current.getSunrise();
    }


    /**
     * Returns the sunset timestamp.
     *
     * @return  String.
     */
    String getSunset() {
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


    /**
     * Returns the weather n days forward, e.g. n = 2 will return the weather
     * on the day after tomorrow.
     *
     * @param n     Which future day to get weather information on.
     * @return      String with the weather information.
     */
    String getWeatherDayN(int n) {

        return null;
    }


    @Override
    public String toString() {
        return getCurrentWeatherString();
    }


    /**
     * Returns a formatted string with the weather information for the next
     * five days.
     *
     * @return  String.
     */
    String getForecastString() {
        // TODO: Make method for day: return today, tommorrow, or weekday.
        String str;
        StringBuilder sb;

        sb = new StringBuilder();

        for (int i = 0; i < dayWeathers.size(); i++) {
            if (i == 0) {
                str = "Today";
            }
            else if (i == 1) {
                str = "Tomorrow";
            }
            else {
                str = dayWeathers.get(i).getWeekday();
            }
            str += ":";
            sb.append(String.format("%1$-10s", str));

            sb.append(dayWeathers.get(i).toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    String getLocationName() {
        return locationName;
    }

}

