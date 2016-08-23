package com.example.uschat.uschat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uschat.uschat.Adapter.KisilerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by sahin on 26.04.2016.
 */
public class KisilerFragment extends PageFragment{
    private static final String KULLANICI_ADI = "KullaniciAdi";
    private static final String MESAJ_GONDERILENLER = "Mesaj_Gonderilenler";
    SharedPreferences sharedpreferences;
    ArrayList<String> tmp;
    ListView lvContent;
    SearchView mSearchView;
    KisilerAdapter kisilerAdapter;



    private List<Message> mMessages = new ArrayList<>();
    private static final String TAG = "ChatActivity";
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.1.104:3003");
            Log.v(TAG, "Fine!");
        } catch (URISyntaxException e) {
            Log.v(TAG, "Error Connecting to IP!" + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        lvContent = (ListView) view.findViewById(R.id.lvContent);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        tmp = new ArrayList<>();

        mSocket.on("uyeler", onNewMessage);

        sharedpreferences = getActivity().getSharedPreferences(KULLANICI_ADI, Context.MODE_PRIVATE);
        String kullaniciAdi = sharedpreferences.getString(KULLANICI_ADI, null);
        mSocket.emit("giris_yapildi", kullaniciAdi);
        //mSocket.on("test",onTest);
        mSocket.connect();
        kisilerAdapter = new KisilerAdapter(getContext(),tmp);
        lvContent.setAdapter(kisilerAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> _tmp = new ArrayList<>();
                for (String s : tmp) {
                    if (s.substring(0, newText.length() > s.length() ? s.length() : newText.length()).equals(newText))
                        _tmp.add(s);
                }
                kisilerAdapter = new KisilerAdapter(getContext(), _tmp);
                lvContent.setAdapter(kisilerAdapter);
                return false;
            }
        });
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),ChatActivity.class);
                i.putExtra("Kime",tmp.get(position));
                startActivity(i);
            }
        });
        return view;
    }

    private Emitter.Listener onTest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tmp.add(":D --> asdasdasd");
                }
            });
        }
    };

    private void addMessage(String KullaniciAdi) {
        tmp.add(KullaniciAdi);
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject jsonObj = (JSONObject) args[0];
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObj.getJSONArray("uyeler");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject data = jsonArray.getJSONObject(i);
                            int UyeID;
                            String Ad;
                            String Soyad;
                            String Eposta;
                            String KullaniciAdi;
                            Log.v(TAG, "INCOMING JSON DATA" + data.toString());
                            try {
                                UyeID = data.getInt("UyeID");
                                Ad = data.getString("Ad");
                                Soyad = data.getString("Soyad");
                                Eposta = data.getString("Eposta");
                                KullaniciAdi = data.getString("KullaniciAdi");

                                Log.v(TAG, "JSON Data to String! Ad:" + Ad);
                                Log.v(TAG, "JSON Data to String! Soyad:" + Soyad);
                                Log.v(TAG, "JSON Data to String! KullaniciAdi:" + KullaniciAdi);
                            } catch (JSONException e) {
                                Log.v(TAG, "Error Getting the JSON Data!" + e.getMessage());
                                return;
                            }

                            addMessage(KullaniciAdi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    kisilerAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}