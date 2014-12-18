package deepjamcam.rpims4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class splashscreen extends Activity {
    private static final String PREFS_NAME = "MyPrefsFile";
    public boolean tutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shared Preferences for tutorial
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        tutorial = settings.getBoolean("tutorial", tutorial);
        setContentView(R.layout.activity_splashscreen);
        Thread splashscreen = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    if(!tutorial) {
                        startActivity(new Intent(getApplicationContext(), howtoplay.class));
                        tutorial = true;
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }
        };
        splashscreen.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splashscreen, menu);
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("tutorial", tutorial);
        editor.commit();
    }
}
