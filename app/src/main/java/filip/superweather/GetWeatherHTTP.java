package filip.superweather;

import android.os.AsyncTask;

class GetWeatherHTTP extends AsyncTask<String, Void, Weather> {
    AsyncResponse delegate = null;

    GetWeatherHTTP() {

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

        weather = new Weather(strings[0]);

        return weather;

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

