package filip.superweather;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetWeatherHTTP extends AsyncTask<String, Void, Weather> {
    private Weather weather;

    public AsyncResponse delegate = null;

    GetWeatherHTTP() {

    }


    @Override
    protected Weather doInBackground(String... strings) {

        weather = new Weather(strings[0]);

        return weather;

    }


    @Override
    protected void onPostExecute(Weather weather) {

        delegate.processFinish(weather);
    }

}

