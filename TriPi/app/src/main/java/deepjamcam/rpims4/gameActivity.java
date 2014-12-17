package deepjamcam.rpims4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

public class gameActivity extends MainActivity implements SensorEventListener {
    public SensorManager mSensorManager;
    public Sensor mLinearAccelerometer;
    public TextView theFinalCountDown;
//    public TextView defaultAcceleration;
    public ImageView fadedFlat;
    public ImageView blueRight;
    public ImageView yellowDown;
    public ImageView redLeft;
    public ImageView greenUp;
    public boolean startState;
    public float currX = 0;
    public float currY = 0;
    public float currZ = 0;
    public int sleepTime = 500;
    public JSONObject jsonObject = new JSONObject();
    public JSONArray allLightObjects = new JSONArray();
    public HashMap<Integer, int[]> lightMap = new HashMap<Integer, int[]>();
    Random rando = new Random();
    public String json;
    public String ipAddr;
    private static final String PREFS_NAME = "MyPrefsFile";
    public boolean wait = false;
    public int gameAttempt = 1;
    public int codeAttemptMiddle = 1;
    public int codeAttemptEnd = 1;
    public int sensitivity = 10;
    public int storedScore;
    public int currScore;
    public String newColor;
    public boolean achievement1;
    public boolean achievement2;
    public boolean achievement3;
    public boolean achievement4;
    public boolean achievement5;
    public MediaPlayer mediaPlayer;
    public int correctMoveCounter;
    public CountDownTimer timer;
    public boolean triCheck;
    public int timerDuration;
    public TextView score;
    public TextView scoreLbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ipAddr = settings.getString("builtIpAddr", ipAddr);
        storedScore = settings.getInt("storedScore", storedScore);
        achievement1 = settings.getBoolean("achievement1", achievement1);
        achievement2 = settings.getBoolean("achievement2", achievement2);
        achievement3 = settings.getBoolean("achievement3", achievement3);
        achievement4 = settings.getBoolean("achievement4", achievement4);
        achievement5 = settings.getBoolean("achievement5", achievement5);

        triCheck = true;
        timerDuration = 3000;

        mediaPlayer = MediaPlayer.create(gameActivity.this, R.raw.tswizzle);
        mediaPlayer.setLooping(true);

        currScore = 0;
        startState = false;

        setContentView(R.layout.activity_game);
        theFinalCountDown = (TextView) findViewById(R.id.theFinalCountDown);
//        defaultAcceleration = (TextView) findViewById(R.id.defaultAcceleration);
        fadedFlat = (ImageView) findViewById(R.id.fadedFlat);
        blueRight = (ImageView) findViewById(R.id.blueRight);
        yellowDown = (ImageView) findViewById(R.id.yellowDown);
        redLeft = (ImageView) findViewById(R.id.redLeft);
        greenUp = (ImageView) findViewById(R.id.greenUp);
        score = (TextView) findViewById(R.id.score);
        scoreLbl = (TextView) findViewById(R.id.scoreLbl);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mLinearAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        int[] tempArray = new int[4];
        for(int i = 0; i < 4; i++) {
            tempArray[i] = 0;
        }

        for(int i = 0; i < 32; i++) {
            lightMap.put(i, tempArray);
        }

        // SLEEP 1 SECONDS HERE ...
        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("Let's Trip");
                mediaPlayer.start();
            }
        }, 0000);

        // SLEEP 1 SECONDS HERE ...
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("3");
            }
        }, 3000);

        // SLEEP 2 SECONDS HERE ...
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("2");
            }
        }, 3750);

        // SLEEP 3 SECONDS HERE ...
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("1");
            }
        }, 4500);

        // SLEEP 4 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("GO!");
                startState = true;
            }
        }, 5250);

        // SLEEP 4 SECONDS HERE ...
        Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {
            public void run() {
                scoreLbl.setText("Score");
                score.setText(currScore + "");
            }
        }, 6000);

        // SLEEP 4.5 SECONDS HERE ...
        Handler lastHandler = new Handler();
        lastHandler.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("");
            }
        }, 6000);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (startState) {
//            defaultAcceleration.setText("X: " + event.values[0] +
//                    "\nY: " + event.values[1] +
//                    "\nZ: " + event.values[2]);

            currX = event.values[0];
            currY = event.values[1];
            currZ = event.values[2];

            if(triCheck == true) {
                triCheck = false;
                new createLights().execute();
                timer = new CountDownTimer(timerDuration, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        gameAttempt++;
                        triCheck = true;
                    }
                }.start();
            }

            if(currX > sensitivity && Math.abs(currX) > Math.abs(currY) && !wait){
                wait = true;
                greenUp.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);
                if(!newColor.equals("green")){
                    correctMoveCounter = 0;
                    gameAttempt++;
                } else {
                    correctMoveCounter++;
                    currScore++;
                    score.setText(currScore + "");

                    if(currScore == 20) {
                        timerDuration = 2000;
                    }

                    if(currScore == 30) {
                        timerDuration = 1500;
                    }

                    if(currScore == 40) {
                        timerDuration = 1000;
                    }

                    if (currScore == 5 && achievement1 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQAw");
                        achievement1 = true;
                    } else if (currScore == 10 && achievement2 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBA");
                        achievement2 = true;
                    } else if (currScore == 15 && achievement3 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBQ");
                        achievement3 = true;
                    } else if (currScore == 20 && achievement4 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBg");
                        achievement4 = true;
                    } else if (currScore == 25 && achievement5 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBw");
                        achievement5 = true;
                    }
                }
                timer.cancel();
                triCheck = true;
                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        greenUp.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = false;
                    }
                }, sleepTime);

            }

            if(currX < -1*sensitivity && Math.abs(currX) > Math.abs(currY) && !wait){
                wait = true;
                yellowDown.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);
                if(!newColor.equals("yellow")){
                    correctMoveCounter = 0;
                    gameAttempt++;
                } else {
                    correctMoveCounter++;
                    currScore++;
                    score.setText(currScore + "");
                    if(currScore == 20) {
                        timerDuration = 2000;
                    }

                    if(currScore == 30) {
                        timerDuration = 1500;
                    }

                    if(currScore == 40) {
                        timerDuration = 1000;
                    }
                    if (currScore == 5 && achievement1 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQAw");
                        achievement1 = true;
                    } else if (currScore == 10 && achievement2 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBA");
                        achievement2 = true;
                    } else if (currScore == 15 && achievement3 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBQ");
                        achievement3 = true;
                    } else if (currScore == 20 && achievement4 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBg");
                        achievement4 = true;
                    } else if (currScore == 25 && achievement5 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBw");
                        achievement5 = true;
                    }
                }
                timer.cancel();
                triCheck = true;
                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        yellowDown.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = false;
                    }
                }, sleepTime);
            }

            if(currY > sensitivity && Math.abs(currX) < Math.abs(currY) && !wait){
                wait = true;
                redLeft.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);
                if(!newColor.equals("red")){
                    correctMoveCounter = 0;
                    gameAttempt++;
                } else {
                    correctMoveCounter++;
                    currScore++;
                    score.setText(currScore + "");
                    if(currScore == 20) {
                        timerDuration = 2000;
                    }

                    if(currScore == 30) {
                        timerDuration = 1500;
                    }

                    if(currScore == 40) {
                        timerDuration = 1000;
                    }
                    if (currScore == 5 && achievement1 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQAw");
                        achievement1 = true;
                    } else if (currScore == 10 && achievement2 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBA");
                        achievement2 = true;
                    } else if (currScore == 15 && achievement3 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBQ");
                        achievement3 = true;
                    } else if (currScore == 20 && achievement4 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBg");
                        achievement4 = true;
                    } else if (currScore == 25 && achievement5 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBw");
                        achievement5 = true;
                    }
                }
                timer.cancel();
                triCheck = true;
                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        redLeft.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = false;
                    }
                }, sleepTime);
            }

            if(currY < -1*sensitivity && Math.abs(currX) < Math.abs(currY) && !wait){
                wait = true;
                blueRight.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);
                if(!newColor.equals("blue")){
                    correctMoveCounter = 0;
                    gameAttempt++;
                } else {
                    correctMoveCounter++;
                    currScore++;
                    score.setText(currScore + "");
                    if(currScore == 20) {
                        timerDuration = 2000;
                    }

                    if(currScore == 30) {
                        timerDuration = 1500;
                    }

                    if(currScore == 40) {
                        timerDuration = 1000;
                    }
                    if (currScore == 5 && achievement1 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQAw");
                        achievement1 = true;
                    } else if (currScore == 10 && achievement2 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBA");
                        achievement2 = true;
                    } else if (currScore == 15 && achievement3 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBQ");
                        achievement3 = true;
                    } else if (currScore == 20 && achievement4 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBg");
                        achievement4 = true;
                    } else if (currScore == 25 && achievement5 == false) {
                        Games.Achievements.unlock(getApiClient(), "CgkInamu4JcWEAIQBw");
                        achievement5 = true;
                    }
                }
                timer.cancel();
                triCheck = true;
                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        blueRight.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = false;
                    }
                }, sleepTime);
            }
        }
    }

    public class createLights extends AsyncTask<String, String,String> {

        // lights start: 1
        // lights end: 32

        @Override
        protected String doInBackground(String... params) {


            int randint = rando.nextInt(4);
            int[] tempArr = new int[3];
            newColor = "";
            if(randint == 0){
                newColor = "red";
                tempArr[0] = 255;
                tempArr[1] = 0;
                tempArr[2] = 0;
            }
            if(randint == 1){
                newColor = "green";
                tempArr[0] = 0;
                tempArr[1] = 255;
                tempArr[2] = 0;
            }
            if(randint == 2){
                newColor = "blue";
                tempArr[0] = 0;
                tempArr[1] = 0;
                tempArr[2] = 255;
            }
            if(randint == 3){
                newColor = "yellow";
                tempArr[0] = 255;
                tempArr[1] = 255;
                tempArr[2] = 0;
            }

            if(gameAttempt == 1){
                codeAttemptMiddle = 1;
                codeAttemptEnd = 33;
            }
            if(gameAttempt == 2){
                codeAttemptMiddle = 3;
                codeAttemptEnd = 31;
            }
            if(gameAttempt == 3){
                codeAttemptMiddle = 5;
                codeAttemptEnd = 29;
            }
            if(gameAttempt == 4){
                codeAttemptMiddle = 7;
                codeAttemptEnd = 27;
            }
            if(gameAttempt == 5){
                codeAttemptMiddle = 9;
                codeAttemptEnd = 25;
            }
            if(gameAttempt == 6){
                codeAttemptMiddle = 11;
                codeAttemptEnd = 23;
            }
            if(gameAttempt == 7){
                codeAttemptMiddle = 13;
                codeAttemptEnd = 21;
            }
            if(gameAttempt == 8){
                codeAttemptMiddle = 15;
                codeAttemptEnd = 19;
            }
            if(gameAttempt == 9){
                codeAttemptMiddle = 16;
                codeAttemptEnd = 18;
            }
            if(gameAttempt == 10){
                codeAttemptMiddle = 16;
                codeAttemptEnd = 17;
            }

            try {

                JSONObject lightObjStart = new JSONObject();
                lightObjStart.put("lightId",new Integer(1));
                lightObjStart.put("red",new Integer(0));
                lightObjStart.put("green",new Integer(0));
                lightObjStart.put("blue",new Integer(0));
                lightObjStart.put("intensity",new Double(0.70));

                JSONObject lightObjMiddle = new JSONObject();
                lightObjMiddle.put("lightId",new Integer(codeAttemptMiddle));
                lightObjMiddle.put("red",new Integer(0));
                lightObjMiddle.put("green",new Integer(0));
                lightObjMiddle.put("blue",new Integer(0));
                lightObjMiddle.put("intensity",new Double(0.70));

                JSONObject lightObjEnd = new JSONObject();
                lightObjEnd.put("lightId",new Integer(codeAttemptEnd));
                lightObjEnd.put("red",new Integer(0));
                lightObjEnd.put("green",new Integer(0));
                lightObjEnd.put("blue",new Integer(0));
                lightObjEnd.put("intensity",new Double(0.70));


                if(gameAttempt > 10){

                    lightObjStart = new JSONObject();
                    lightObjStart.put("lightId",new Integer(1));
                    lightObjStart.put("red",new Integer(255));
                    lightObjStart.put("green",new Integer(255));
                    lightObjStart.put("blue",new Integer(255));
                    lightObjStart.put("intensity",new Double(0.20));

                    lightObjMiddle = new JSONObject();
                    lightObjMiddle.put("lightId",new Integer(codeAttemptMiddle));
                    lightObjMiddle.put("red",new Integer(255));
                    lightObjMiddle.put("green",new Integer(255));
                    lightObjMiddle.put("blue",new Integer(255));
                    lightObjMiddle.put("intensity",new Double(0.20));

                    lightObjEnd = new JSONObject();
                    lightObjEnd.put("lightId",new Integer(codeAttemptEnd));
                    lightObjEnd.put("red",new Integer(255));
                    lightObjEnd.put("green",new Integer(255));
                    lightObjEnd.put("blue",new Integer(255));
                    lightObjEnd.put("intensity",new Double(0.20));

                    gameAttempt = 1;

                    if (currScore > storedScore) {
                        storedScore = currScore;
                        Games.Leaderboards.submitScore(getApiClient(), "CgkInamu4JcWEAIQAg", storedScore);
                    }
//                    System.out.println("Current Score: " + currScore);
//                    System.out.println("Stored Score: " + storedScore);
                    timer.cancel();
                    mediaPlayer.stop();
                    startState = false;
                    Intent gameIntent = new Intent(gameActivity.this, MainActivity.class);
                    gameActivity.this.startActivity(gameIntent);

                }else{
                    lightObjStart = new JSONObject();
                    lightObjStart.put("lightId",new Integer(1));
                    lightObjStart.put("red",new Integer(255));
                    lightObjStart.put("green",new Integer(255));
                    lightObjStart.put("blue",new Integer(255));
                    lightObjStart.put("intensity",new Double(0.20));

                    lightObjMiddle = new JSONObject();
                    lightObjMiddle.put("lightId",new Integer(codeAttemptMiddle));
                    lightObjMiddle.put("red",tempArr[0]);
                    lightObjMiddle.put("green",tempArr[1]);
                    lightObjMiddle.put("blue",tempArr[2]);
                    lightObjMiddle.put("intensity",new Double(0.70));

                    lightObjEnd = new JSONObject();
                    lightObjEnd.put("lightId",new Integer(codeAttemptEnd));
                    lightObjEnd.put("red",new Integer(255));
                    lightObjEnd.put("green",new Integer(255));
                    lightObjEnd.put("blue",new Integer(255));
                    lightObjEnd.put("intensity",new Double(0.20));
                }
                allLightObjects.put(lightObjStart);
                allLightObjects.put(lightObjMiddle);
                allLightObjects.put(lightObjEnd);

                jsonObject.put("lights", allLightObjects);
                jsonObject.put("propagate", true);
                json = jsonObject.toString();
                System.out.println(json);
                new httpPostRequest().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(correctMoveCounter >= 10){
                if((gameAttempt-1) <= 0){
                    gameAttempt = 0;
                }else{
                    gameAttempt--;
                }
                correctMoveCounter = 0;
            }

            return null;
        }
    }

    private class httpPostRequest extends AsyncTask<String, String,String> {
        protected String doInBackground(String... urls) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(ipAddr);
                StringEntity se = new StringEntity(json);
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Intent gameIntent = new Intent(gameActivity.this, settings.class);
                gameActivity.this.startActivity(gameIntent);
            } catch (IOException e) {
                e.printStackTrace();
                Intent gameIntent = new Intent(gameActivity.this, settings.class);
                gameActivity.this.startActivity(gameIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent gameIntent = new Intent(gameActivity.this, settings.class);
                gameActivity.this.startActivity(gameIntent);
            }
            return null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("storedScore", storedScore);
        editor.putBoolean("achievement1", achievement1);
        editor.putBoolean("achievement2", achievement2);
        editor.putBoolean("achievement3", achievement3);
        editor.putBoolean("achievement4", achievement4);
        editor.putBoolean("achievement5", achievement5);
        editor.commit();

        mediaPlayer.stop();
        timer.cancel();
        startState = false;
    }
}