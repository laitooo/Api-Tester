package zxc.laitooo.apitester;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    TextView result;
    WebView html;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //getSupportActionBar().setHomeButtonEnabled(true);

        html = (WebView)findViewById(R.id.result_webview);
        result = (TextView) findViewById(R.id.result_text);

        html.setVisibility(View.INVISIBLE);

        Intent i = getIntent();
        int style = i.getIntExtra("style",0);
        String resu = i.getStringExtra("resu");
        boolean error = i.getBooleanExtra("error",true);
        String error_log = i.getStringExtra("error_log");
        //Toast.makeText(getApplicationContext(),error_log,Toast.LENGTH_LONG).show();

        if (error && error_log.equals("no log")){
            result.setText(R.string.network_error);
            result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
            html.setVisibility(View.INVISIBLE);
            result.setVisibility(View.VISIBLE);
        }else if (error && !error_log.equals("no log")){
            if (error_log.equals("403"))
                result.setText("Error 403 \n You don't have permission to access this address\n\n " +
                        "tip : try to use VPN");
            else if (error_log.equals("404"))
                result.setText("Error 404 \n Not able to find this address");
            else
                result.setText("Error Code " + error_log);
            result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
            html.setVisibility(View.INVISIBLE);
            result.setVisibility(View.VISIBLE);
        }else{
            if (style == 0) {
                result.setText("Result : " + resu);
                html.setVisibility(View.INVISIBLE);
                result.setVisibility(View.VISIBLE);
                result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            } else if (style == 1) {
                html.setVisibility(View.VISIBLE);
                try {
                    html.loadData(resu, "text/html", "UTF-8");
                }catch (Exception e){
                    result.setText(R.string.not_html);
                    html.setVisibility(View.INVISIBLE);
                    result.setVisibility(View.VISIBLE);
                    result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                }
                result.setVisibility(View.INVISIBLE);
            }else {
                try {
                    JSONArray array = new JSONArray(resu);
                    resu = array.toString(4);
                }catch (JSONException e){
                    try {
                        JSONObject array = new JSONObject(resu);
                        resu = array.toString(4);
                    }catch (JSONException e2) {
                        resu = getString(R.string.not_json);
                    }
                }
                result.setText(resu);
                html.setVisibility(View.INVISIBLE);
                result.setVisibility(View.VISIBLE);
                result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
            }
        }

    }


}
