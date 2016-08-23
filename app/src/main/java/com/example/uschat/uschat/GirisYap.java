package com.example.uschat.uschat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahin on 25.04.2016.
 */
public class GirisYap extends AppCompatActivity {
    private static final String KULLANICI_ADI = "KullaniciAdi";
    SharedPreferences sharedpreferences;
    private static final String URL = "http://192.168.1.104:31/UyeMi.php";
    EditText KullaniciAdi,Sifre;
    Button girisYap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.giris_layout);
        install_elements();
    }
    private void install_elements()
    {
        KullaniciAdi = (EditText) findViewById(R.id.etKullaniciAdi_gy);
        Sifre = (EditText) findViewById(R.id.etSifre_gy);
        girisYap = (Button) findViewById(R.id.btn_gy);
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kullaniciAdi = KullaniciAdi.getText().toString();
                String sifre = Sifre.getText().toString();

                String[] bilgiler = {URL,kullaniciAdi,sifre};
                new UyeKontrol().execute(bilgiler);

                Intent i = new Intent(GirisYap.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    public class UyeKontrol extends AsyncTask<String, Integer, Long> {
        HttpPost httppost;
        HttpClient httpclient;
        HttpResponse httpResponse;
        ProgressDialog progDailog;
        String strJson = null;
        InputStream is = null;
        String[] arr=null;

        public UyeKontrol() {

        }

        @Override
        protected Long doInBackground(String... params) {

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost ();
            try{
                JSONObject obj=new JSONObject();
                obj.put("KullaniciAdi",params[1].toString());
                obj.put("Sifre",params[2].toString());

                HttpEntity httpEntity;
                StringEntity stringEntity=new StringEntity(obj.toString(), HTTP.UTF_8);
                stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpEntity=stringEntity;
                httppost.setEntity(httpEntity);
                //RESTful Web Servisinin baglancağı url yi veriyoruz...
                httppost.setURI(new URI(params[0].toString()));
                httppost.setHeader("Content-type", "application/json");
                // HttpEntity tutulan dataların HttpResponse tarafından çalıstırılmasını saglama..
                httpResponse = httpclient.execute(httppost);
                httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line=reader.readLine())!=null)
                {
                    sb.append(line);
                }
                is.close();
                strJson = sb.toString();

                strJson = strJson.replace("{", "");
                strJson = strJson.replace("}", "");
                strJson = strJson.replace("\"", "");
                strJson = strJson.replace("KullaniciAdi:", "");
                strJson = strJson.replace("Sifre:", "");
                arr = strJson.split(",");


                sharedpreferences = getSharedPreferences(KULLANICI_ADI, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(KULLANICI_ADI, arr[0].toString());
                editor.commit();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progDailog = new ProgressDialog(GirisYap.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();*/
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            /*progDailog.dismiss();*/
        }

    }


}
