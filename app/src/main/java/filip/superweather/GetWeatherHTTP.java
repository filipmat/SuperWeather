package filip.superweather;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class GetWeatherHTTP extends AsyncTask<String, Void, Weather> {
    AsyncResponse delegate = null;
    Context context;

    GetWeatherHTTP(Context context) {
        this.context = context;
    }


    /**
     * Get weather information.
     *
     * @param strings   strings[0] should contain the full url.
     * @return          Weather object.
     */
    @Override
    protected Weather doInBackground(String... strings) {
        Weather weather;
        String apiKey;

        apiKey = getAPIKey();

        weather = new Weather(strings[0], apiKey);

        return weather;

    }


    private String getAPIKey() {
        String key;
        Properties prop;
        InputStream input;

        prop = new Properties();
        input = null;
        key = "";

        try {
            input = context.getAssets().open("config.properties");

            prop.load(input);

            key = prop.getProperty("apikey");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return key;

        }
    }


    /**
     * After execution processFinish() of the main activity is called..
     *
     * @param weather   Weather object.
     */
    @Override
    protected void onPostExecute(Weather weather) {
        delegate.processFinish(weather);
    }

}

