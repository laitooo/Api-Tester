package zxc.laitooo.apitester;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;

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

        if (error || resu.equals("")){
            result.setText(R.string.network_error);
            result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
            html.setVisibility(View.INVISIBLE);
            result.setVisibility(View.VISIBLE);
        }else {
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
                }catch (Exception e){
                    resu = getString(R.string.not_json);
                }
                result.setText(resu);
                html.setVisibility(View.INVISIBLE);
                result.setVisibility(View.VISIBLE);
                result.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
            }
        }

    }
}
