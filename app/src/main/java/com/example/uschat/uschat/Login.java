package com.example.uschat.uschat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by sahin on 23.04.2016.
 */
public class Login extends AppCompatActivity {
    private static final String URL = "http://192.168.239.1:31/uyeEkle.php";
    EditText Ad,Soyad,KullaniciAdi,Email,Sifre,SifreTekrari;
    Button kayitOl,girisYap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        install_elements();
    }
    private void install_elements()
    {
        Ad = (EditText) findViewById(R.id.etAd);
        Soyad = (EditText) findViewById(R.id.etSoyad);
        KullaniciAdi = (EditText) findViewById(R.id.etKullaniciAdi);
        Email = (EditText) findViewById(R.id.etEmail);
        Sifre = (EditText) findViewById(R.id.etSifre);
        SifreTekrari = (EditText) findViewById(R.id.etSifreTekrari);
        girisYap = (Button) findViewById(R.id.btn_girisyap);
        kayitOl = (Button) findViewById(R.id.btn_kayitol);
        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* String ad = Ad.getText().toString();
                String soyad = Soyad.getText().toString();
                String email = Email.getText().toString();
                String kullaniciAdi = KullaniciAdi.getText().toString();
                String sifre = Sifre.getText().toString();
*/

                String ad = "asd";
                String soyad = "sad";
                String email = "dsa";
                String kullaniciAdi = "dasd";
                String sifre = "asdasd";

                String[] bilgiler = {URL,ad,soyad,email,kullaniciAdi,sifre};
                new UyeEkle().execute(bilgiler);
            }
        });
        girisYap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,GirisYap.class);
                startActivity(i);
            }
        });
    }


    public class UyeEkle extends AsyncTask<String, Integer, Long> {
        List<NameValuePair> uyelikBilgileri;
        HttpPost httppost;
        HttpClient httpclient;
        ProgressDialog progDailog;
        public UyeEkle() {

        }

        @Override
        protected Long doInBackground(String... params) {

            httpclient = new DefaultHttpClient();
            httppost = new HttpPost ();
            try{
                JSONObject obj=new JSONObject();
                obj.put("Adi",params[1].toString());
                obj.put("Soyadi",params[2].toString());
                obj.put("Email",params[3].toString());
                obj.put("KullaniciAdi",params[4].toString());
                obj.put("Sifre",params[5].toString());

                HttpEntity httpEntity;
                StringEntity stringEntity=new StringEntity(obj.toString(), HTTP.UTF_8);
                stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpEntity=stringEntity;
                httppost.setEntity(httpEntity);
                //RESTful Web Servisinin baglancağı url yi veriyoruz...
                httppost.setURI(new URI(params[0].toString()));
                httppost.setHeader("Content-type", "application/json");
                // HttpEntity tutulan dataların HttpResponse tarafından çalıstırılmasını saglama..
                httpclient.execute(httppost);

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
            progDailog = new ProgressDialog(Login.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            progDailog.dismiss();
        }

    }

}
