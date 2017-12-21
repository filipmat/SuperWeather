package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Weather weather;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather);

        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            weather = (Weather) bundle.getSerializable(
                    "weather_serialized");
            displayWeather(weather.getForecastString());
            setTitle(weather.getLocationName() + " weather forecast");

        } catch (Exception e) {
            displayWeather(e.toString());
        }
    }


    /**
     * Change the text in the TextView to the weather info.
     *
     * @param message   String with weather information.
     */
    private void displayWeather(String message) {
        TextView textView = findViewById(R.id.textViewWeather);
        textView.setText(message);
    }


    /**
     * Activity finish.
     */
    @Override
    public void finish() {
        Intent data = new Intent();

        setResult(RESULT_OK, data);
        super.finish();
    }

}
