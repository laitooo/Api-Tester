package zxc.laitooo.apitester;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText url;
    RadioButton get,post;
    RecyclerView params;
    Button send;
    RadioButton SText,SHtml,SJson;

    ParamsAdapter adapter;
    ArrayList<Param> list;

    ProgressDialog dialog;

    boolean isGet;
    String link,resu;

    RequestQueue queue;

    int style = 0;
    boolean error = false;
    String error_log = "no log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(getApplicationContext());

        url = (EditText)findViewById(R.id.url);
        get = (RadioButton) findViewById(R.id.get);
        post = (RadioButton)findViewById(R.id.post);
        params = (RecyclerView) findViewById(R.id.recycler_params);
        send = (Button) findViewById(R.id.send);
        SText = (RadioButton)findViewById(R.id.style_text);
        SHtml = (RadioButton)findViewById(R.id.style_html);
        SJson = (RadioButton)findViewById(R.id.style_json);


        isGet = true;


        list = new ArrayList<>();
        list.add(new Param("",""));
        adapter = new ParamsAdapter(list,getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        params.setAdapter(adapter);
        params.setLayoutManager(linearLayoutManager);


        get.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    post.setChecked(false);
                    isGet = true;
                }
            }
        });

        post.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    get.setChecked(false);
                    isGet = false;
                }
            }
        });

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Loading");

        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                link = s.toString();
            }
        });

        //http://laitooosan.000webhostapp.com/test.php


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Url is empty",Toast.LENGTH_SHORT).show();
                }else {
                    if (!url.getText().toString().startsWith("https://") &&
                            !url.getText().toString().startsWith("http://"))
                        url.setText("https://" + url.getText().toString());

                    if (dialog != null)
                        dialog.show();
                    if (isOnline())
                        new sendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        /*result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),resu,Toast.LENGTH_LONG).show();
            }
        });*/


        SText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SHtml.setChecked(false);
                    SJson.setChecked(false);
                    style = 0;
                }
            }
        });

        SHtml.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SText.setChecked(false);
                    SJson.setChecked(false);
                    style = 1;
                }
            }
        });

        SJson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SHtml.setChecked(false);
                    SText.setChecked(false);
                    style = 2;
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public class sendRequest extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            resu = "";
        }

        @Override
        protected Object doInBackground(final Object[] params) {
            StringRequest request = new StringRequest(isGet ? Request.Method.GET : Request.Method.POST,
                    link,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            resu += s;
                            error = false;
                            Log.e("NO ERROR","message : " + s);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            resu += volleyError.getMessage();
                            error = true;
                            try {
                                error_log = String.valueOf(volleyError.networkResponse.statusCode);
                                Log.e("ERM","f" + volleyError.networkResponse.statusCode);
                            }catch (NullPointerException e){
                                error_log = "no log";
                            }

                            try {
                                if (volleyError.getMessage().contains("javax.net.ssl.SSLHandshakeException:"))
                                    error_log = "SSL Handshake Exception   \nSSL Protocol Exception \n\n" +
                                            "tip: try to use \"http://\" instead of \"https://\"";
                            }catch (NullPointerException e){

                            }
                            Log.e("ERRORRORORO","Eee" + volleyError.getMessage());
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    for (Param p:list){
                        if (!(p.getKey().equals("") && p.getValue().equals("")))
                            map.put(p.getKey(),p.getValue());
                    }
                    return map;
                }
            };


            queue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            handler.postDelayed(runnable,5000);
        }
    };

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dialog != null)
                dialog.dismiss();
            Intent i = new Intent(getApplicationContext(),ResultActivity.class);
            i.putExtra("style",style);
            i.putExtra("resu",resu);
            i.putExtra("error",error);
            i.putExtra("error_log",error_log);
            startActivity(i);
        }
    };

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
