package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AsyncResponse{
    public final static String EXTRA_MESSAGE = "filip.superweather.MESSAGE";
    static final int DISPLAY_WEATHER_REQUEST = 1;

    private Weather weather;

    private GetWeatherHTTP getW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getW = new GetWeatherHTTP();
        getW.delegate = this;

    }

    @Override
    public void processFinish(Weather wh) {
        String message;

        weather = wh;

        message = wh.getCurrentWeatherString();

        Intent intent = new Intent(
                this, DisplayWeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, DISPLAY_WEATHER_REQUEST);

    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultcode, Intent data) {

        if (requestCode == DISPLAY_WEATHER_REQUEST) {
            TextView textView = findViewById(R.id.textView);
            textView.setText("");
        }

    }


    public void getWeatherCallback(View view) {
        String cityID;

        getW = new GetWeatherHTTP();
        getW.delegate = this;

        cityID = view.getTag().toString();

        TextView textView = findViewById(R.id.textView);
        textView.setText(R.string.retrieving_weather);

        getW.execute(cityID);


    }

}
