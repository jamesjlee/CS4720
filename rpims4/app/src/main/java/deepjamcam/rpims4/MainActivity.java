package deepjamcam.rpims4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public Button startButt;
    public Button howtoplay;
//    public Button scoreboardButt;
    public Button settingsButt;
    private static final String PREFS_NAME = "MyPrefsFile";
    public String ipAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startButt = (Button) findViewById(R.id.startButt);
        howtoplay = (Button) findViewById(R.id.howtoplay);
//        scoreboardButt = (Button) findViewById(R.id.howtoplay);
        settingsButt = (Button) findViewById(R.id.settings);

        startButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this, gameActivity.class);
                MainActivity.this.startActivity(gameIntent);
            }
        });

        howtoplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this, howtoplay.class);
                MainActivity.this.startActivity(gameIntent);
            }
        });



//        scoreboardButt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
        settingsButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this, settings.class);
                MainActivity.this.startActivity(gameIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
