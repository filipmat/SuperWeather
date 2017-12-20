package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "filip.superweather.MESSAGE";

    private String cityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String stockholmID, weatherNow;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityID = "2673723";
    }


    public void getWeatherCallback(View view) {
        Intent intent = new Intent(
                this, DisplayWeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE, cityID);
        startActivity(intent);
    }

}
