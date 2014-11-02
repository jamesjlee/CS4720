package deepjamcam.rpims3;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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


public class MainActivity extends Activity {
    public EditText ipInput;
    public EditText lightIdInput;
    public EditText redInput;
    public EditText blueInput;
    public EditText greenInput;
    public EditText intensityInput;
    public EditText propagateInput;
    public TextView test;
    public Button post;
    public Button setRed;
    public Button setGreen;
    public Button setBlue;
    public Button setOrange;
    public String ipAddr;
    public String lightId;
    public String red;
    public String blue;
    public String green;
    public String intensity;
    public String propagate;
    public String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ipInput = (EditText) findViewById(R.id.ipInput);
        lightIdInput = (EditText) findViewById(R.id.lightIdInput);
        redInput = (EditText) findViewById(R.id.redInput);
        blueInput = (EditText) findViewById(R.id.blueInput);
        greenInput = (EditText) findViewById(R.id.greenInput);
        intensityInput = (EditText) findViewById(R.id.intensityInput);
        propagateInput = (EditText) findViewById(R.id.propagateInput);
        test = (TextView) findViewById(R.id.test);
        post = (Button) findViewById(R.id.postButton);
        setRed = (Button) findViewById(R.id.red);
        setGreen = (Button) findViewById(R.id.green);
        setBlue = (Button) findViewById(R.id.blue);
        setOrange = (Button) findViewById(R.id.orange);

        setRed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightIdInput.setText("1");
                redInput.setText("255.0");
                greenInput.setText("0.0");
                blueInput.setText("0.0");
                intensityInput.setText(".50");
                propagateInput.setText("true");
            }
        });

        setGreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightIdInput.setText("1");
                redInput.setText("0.0");
                greenInput.setText("255.0");
                blueInput.setText("0.0");
                intensityInput.setText(".50");
                propagateInput.setText("true");
            }
        });

        setBlue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightIdInput.setText("1");
                redInput.setText("0.0");
                greenInput.setText("0.0");
                blueInput.setText("255.0");
                intensityInput.setText(".50");
                propagateInput.setText("true");
            }
        });

        setOrange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lightIdInput.setText("1");
                redInput.setText("255.0");
                greenInput.setText("165.0");
                blueInput.setText("0.0");
                intensityInput.setText(".50");
                propagateInput.setText("true");
            }
        });

        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                JSONObject jsonObj = new JSONObject();
                JSONObject lightObj = new JSONObject();
                JSONArray lightsArr = new JSONArray();
                try {
                    ipAddr = ipInput.getText().toString();
                    lightId = lightIdInput.getText().toString();
                    red = redInput.getText().toString();
                    blue = blueInput.getText().toString();
                    green = greenInput.getText().toString();
                    intensity = intensityInput.getText().toString();
                    propagate = propagateInput.getText().toString();
                    lightObj.put("lightId", lightId);
                    lightObj.put("red", red);
                    lightObj.put("blue", blue);
                    lightObj.put("green", green);
                    lightObj.put("intensity", intensity);
                    lightsArr.put(lightObj);
                    jsonObj.put("lights", lightsArr);
                    jsonObj.put("propagate", propagate);
                    json = jsonObj.toString();
                    new httpPostRequest().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                test.setText(jsonObj.toString());
            }
        });
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

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
