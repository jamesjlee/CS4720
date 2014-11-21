package deepjamcam.rpims4;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class gameActivity extends Activity implements SensorEventListener {
    public SensorManager mSensorManager;
    public Sensor mLinearAccelerometer;
    public TextView acceleration;
    public TextView theFinalCountDown;
    public TextView defaultAcceleration;
    public ImageView fadedFlat;
    public ImageView blueRight;
    public ImageView yellowDown;
    public ImageView redLeft;
    public ImageView greenUp;
    public boolean startState;
    public float currX = 0;
    public float currY = 0;
    public float currZ = 0;
    public boolean wait = true;
    public int sleepTime = 500;
    public int sensitivity = 10;
    public JSONObject jsonObj = new JSONObject();
    public JSONArray lightsArr = new JSONArray();
    public ArrayList<Integer> lightArray = new ArrayList<Integer>();
    public HashMap<Integer, int[]> lightMap = new HashMap<Integer, int[]>();
    String[] colorsArray = new String[32];
    Random rando = new Random();
    int iter = 0;
    public String json;
    public String ipAddr;
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ipAddr = settings.getString("builtIpAddr", ipAddr);

        startState = false;
        setContentView(R.layout.activity_game);
        theFinalCountDown = (TextView) findViewById(R.id.theFinalCountDown);
        acceleration = (TextView) findViewById(R.id.acceleration);
        defaultAcceleration = (TextView) findViewById(R.id.defaultAcceleration);
        fadedFlat = (ImageView) findViewById(R.id.fadedFlat);
        blueRight = (ImageView) findViewById(R.id.blueRight);
        yellowDown = (ImageView) findViewById(R.id.yellowDown);
        redLeft = (ImageView) findViewById(R.id.redLeft);
        greenUp = (ImageView) findViewById(R.id.greenUp);

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
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("3");
            }
        }, 1000);

        // SLEEP 2 SECONDS HERE ...
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("2");
            }
        }, 2000);

        // SLEEP 3 SECONDS HERE ...
        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("1");
            }
        }, 3000);

        // SLEEP 4 SECONDS HERE ...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("GO!");
                startState = true;
            }
        }, 4000);

        // SLEEP 4.5 SECONDS HERE ...
        Handler lastHandler = new Handler();
        lastHandler.postDelayed(new Runnable() {
            public void run() {
                theFinalCountDown.setText("");
            }
        }, 4500);

            // SLEEP 5 SECONDS HERE ...
            Handler handler4 = new Handler();
//            final int finalI = i;
            handler4.postDelayed(new Runnable() {
                public void run() {
                    try {

                        createLights();
//                        if(finalI >31) {
//                            Thread.sleep(10);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (startState) {

            defaultAcceleration.setText("X: " + event.values[0] +
                    "\nY: " + event.values[1] +
                    "\nZ: " + event.values[2]);

            currX = event.values[0];
            currY = event.values[1];
            currZ = event.values[2];

            if(currX > sensitivity && Math.abs(currX) > Math.abs(currY) && wait){
                wait = false;
                blueRight.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);

                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        blueRight.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = true;
                    }
                }, sleepTime);

            }

            if(currX < -1*sensitivity && Math.abs(currX) > Math.abs(currY) && wait){
                wait = false;
                redLeft.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);

                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        redLeft.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = true;
                    }
                }, sleepTime);
            }

            if(currY > sensitivity && Math.abs(currX) < Math.abs(currY) && wait){
                wait = false;
                greenUp.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);

                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        greenUp.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = true;
                    }
                }, sleepTime);
            }

            if(currY < -1*sensitivity && Math.abs(currX) < Math.abs(currY) && wait){
                wait = false;
                yellowDown.setVisibility(View.VISIBLE);
                fadedFlat.setVisibility(View.INVISIBLE);

                // SLEEP 1 SECOND HERE ...
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        yellowDown.setVisibility(View.INVISIBLE);
                        fadedFlat.setVisibility(View.VISIBLE);
                        wait = true;
                    }
                }, sleepTime);
            }
        }
    }

    public void createLights() throws JSONException {
        iter--;
        if(iter<0){
            iter=31;
        }

        int randint = rando.nextInt(4);
        int[] tempArr = new int[4];
        String newColor = "";
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
        if(randint == 4){
            newColor = "blank";
            tempArr[0] = 0;
            tempArr[1] = 0;
            tempArr[2] = 0;
        }
        tempArr[3] = 100;
        colorsArray[iter] = newColor;

        lightMap.put(iter, tempArr);

        Collections.sort(lightArray);
//            for (int i = 0; i < 32; i++) {
                JSONObject lightObj = new JSONObject();
//                lightObj.put("lightId", i);
                lightObj.put("lightId", 1);
                lightObj.put("red", 0);
                lightObj.put("blue", 0);
//                lightObj.put("red", lightMap.get((iter + i)%32)[0]);
//                lightObj.put("green", lightMap.get((iter + i)%32)[1]);
                lightObj.put("green", 255);
//                lightObj.put("blue", lightMap.get((iter + i)%32)[2]);
//                lightObj.put("intensity", ((double) lightMap.get((iter + i)%32)[3]) / 100);
                lightObj.put("intensity", 0.75);
//                lightObj.put("lightId", lightArray.get(i));
                lightsArr.put(lightObj);
//            }

            jsonObj.put("lights", lightsArr);
            jsonObj.put("propagate", true);
            json = jsonObj.toString();
            new httpPostRequest().execute();
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
//                Context context = getApplicationContext();
//                CharSequence text = "Posted Successfully.";
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
