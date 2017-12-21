package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AsyncResponse{
    public final static String EXTRA_MESSAGE = "filip.superweather.MESSAGE";
    private static final int DISPLAY_WEATHER_REQUEST = 1;

    private Weather weather;

    private GetWeatherHTTP getW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getW = new GetWeatherHTTP();
        getW.delegate = this;

    }


    /**
     * Called when the AsyncTask for retrieving weather data is finished.
     * Gets the current weather and starts an activity that displays it.
     *
     * @param wh    Weather object.
     */
    @Override
    public void processFinish(Weather wh) {
        weather = wh;

        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable("weather_serialized", weather);
            Intent intent = new Intent(
                    this, DisplayWeatherActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, DISPLAY_WEATHER_REQUEST);

        } catch (Exception e) {
            TextView textView = findViewById(R.id.textView);
            textView.setText(e.toString());
        }

    }


    /**
     * Set the text view to display an empty string.
     *
     * @param requestCode   Request code.
     * @param resultcode    Result code.
     * @param data          Intent data.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultcode, Intent data) {

        if (requestCode == DISPLAY_WEATHER_REQUEST) {
            TextView textView = findViewById(R.id.textView);
            textView.setText("");
        }

    }


    /**
     * Called when pressing a weather button. Gets weather information.
     *
     * @param view  The pressed button.
     */
    public void getWeatherCallback(View view) {
        String cityID;

        TextView textView = findViewById(R.id.textView);
        textView.setText(R.string.retrieving_weather);

        cityID = view.getTag().toString();

        getW = new GetWeatherHTTP();
        getW.delegate = this;
        getW.execute(cityID);

    }

}
