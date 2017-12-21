package filip.superweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "filip.superweather.MESSAGE";

    private String stockholmID, visbyID, sigtunaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockholmID = "2673730";
        visbyID = "2662689";
        sigtunaID = "2679302";

    }


    public void getWeatherCallback(View view) {
        String message;

        message = view.getTag().toString();

        Intent intent = new Intent(
                this, DisplayWeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
