package filip.superweather;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetWeatherHTTP extends AsyncTask<String, Void, String> {
    private TextView textView;
    private Weather weather;

    GetWeatherHTTP(TextView textView) {
        this.textView = textView;

    }


    @Override
    protected String doInBackground(String... strings) {

        weather = new Weather(strings[0]);

        return weather.getCurrentWeatherString();

    }


    @Override
    protected void onPostExecute(String weatherStr) {

        textView.setText(weatherStr);
    }

}

