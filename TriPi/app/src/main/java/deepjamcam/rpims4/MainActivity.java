package deepjamcam.rpims4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.*;

public class MainActivity extends BaseGameActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    public Button startButt;
    public Button leaderboardButt;
    public Button settingsButt;
    public Button achievementsButt;
    private static final String PREFS_NAME = "MyPrefsFile";
    public int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButt = (Button) findViewById(R.id.startButt);
        leaderboardButt = (Button) findViewById(R.id.leaderboard);
        settingsButt = (Button) findViewById(R.id.settingsButt);
        achievementsButt = (Button) findViewById(R.id.achievements);

        startButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this, gameActivity.class);
                MainActivity.this.startActivity(gameIntent);
            }
        });

        leaderboardButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                        "CgkInamu4JcWEAIQAg"), 1);
            }
        });

        settingsButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainActivity.this, settings.class);
                MainActivity.this.startActivity(gameIntent);
            }
        });

        achievementsButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 2);
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

        return super.onOptionsItemSelected(item);
    }

//    public void onSignInSucceeded() {
//        findViewById(R.id.scoreBoardButt).setVisibility(View.GONE);
//        findViewById(R.id.signOutButt).setVisibility(View.VISIBLE);
//    }
//
//    public void onSignInFailed() {
//        findViewById(R.id.scoreBoardButt).setVisibility(View.VISIBLE);
//        findViewById(R.id.signOutButt).setVisibility(View.GONE);
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mGoogleApiClient.disconnect();
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
