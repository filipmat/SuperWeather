package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String message;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_weather);

        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        displayWeather(message);
    }

    private void displayWeather(String message) {
        TextView textView = findViewById(R.id.textViewWeather);
        textView.setText(message);
    }

    @Override
    public void finish() {
        Intent data = new Intent();

        setResult(RESULT_OK, data);
        super.finish();
    }

}
