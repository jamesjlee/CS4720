package deepjamcam.rpims4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class settings extends Activity {
    private static final String PREFS_NAME = "MyPrefsFile";
    public EditText ip1;
    public EditText ip2;
    public EditText ip3;
    public EditText ip4;
    public String ipAddr1;
    public String ipAddr2;
    public String ipAddr3;
    public String ipAddr4;
    public String builtIpAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ip1 = (EditText) findViewById(R.id.ip1);
        ip2 = (EditText) findViewById(R.id.ip2);
        ip3 = (EditText) findViewById(R.id.ip3);
        ip4 = (EditText) findViewById(R.id.ip4);

        ip1.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                ipAddr1 = ip1.getText().toString();
                builtIpAddr = "http://" + ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() + "/rpi";
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ip2.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                ipAddr2 = ip2.getText().toString();
                builtIpAddr = "http://" + ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() + "/rpi";
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ip3.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                ipAddr3 = ip3.getText().toString();
                builtIpAddr = "http://" + ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() + "/rpi";
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ip4.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                ipAddr4 = ip4.getText().toString();
                builtIpAddr = "http://" + ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() + "/rpi";
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        builtIpAddr = settings.getString("builtIpAddr", builtIpAddr);
        ipAddr1 = settings.getString("ipAddr1", ipAddr1);
        ipAddr2 = settings.getString("ipAddr2", ipAddr2);
        ipAddr3 = settings.getString("ipAddr3", ipAddr3);
        ipAddr4 = settings.getString("ipAddr4", ipAddr4);
        ip1.setText(ipAddr1);
        ip2.setText(ipAddr2);
        ip3.setText(ipAddr3);
        ip4.setText(ipAddr4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
        editor.putString("builtIpAddr", builtIpAddr);
        editor.putString("ipAddr1", ipAddr1);
        editor.putString("ipAddr2", ipAddr2);
        editor.putString("ipAddr3", ipAddr3);
        editor.putString("ipAddr4", ipAddr4);
        editor.commit();
    }
}
