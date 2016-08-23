package com.example.uschat.uschat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uschat.uschat.Adapter.ChatAdapter;

import org.json.JSONException;
import org.json.JSONObject;

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
public class ChatActivity extends AppCompatActivity {
    private static final String GELEN= "0";
    private static final String GONDEREN = "1";
    private static final String KULLANICI_ADI = "KullaniciAdi";
    private static final String MESAJ_GONDERILENLER = "Mesaj_Gonderilenler";
    SharedPreferences sharedpreferences;
    ArrayList<String[]> yazi;
    ChatAdapter adptr;
    ListView lvChat;
    EditText etBurayaYaz;

    String Kullanicilar = "";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        final String kullaniciAdi = getIntent().getExtras().getString("Kime");

        lvChat = (ListView) findViewById(R.id.lvChat);
        etBurayaYaz = (EditText) findViewById(R.id.etBurayaYaz);
        yazi = new ArrayList<>();
        yazi.add(new String[]{"Sa",GELEN});
        yazi.add(new String[]{"As", GONDEREN});
        adptr = new ChatAdapter(this,yazi);
        lvChat.setAdapter(adptr);
        etBurayaYaz.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getApplicationContext(), etBurayaYaz.getText().toString(), Toast.LENGTH_SHORT).show();

                    String mesaj = kullaniciAdi.toString()+ "," + etBurayaYaz.getText();
                    yazi.add(new String[]{etBurayaYaz.getText().toString(),GONDEREN});
                    mSocket.emit("send", mesaj);
                    etBurayaYaz.setText("");
                    return true;
                }
                return false;
            }
        });
        /*
        yazi.add(new String[]{"Assdadas", GONDEREN});
        yazi.add(new String[]{"Adasdasds",GELEN});
        adptr.notifyDataSetChanged();
        */

        mSocket.on("new message", onNewMessage);
        mSocket.connect();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String)args[0];
                    yazi.add(new String[]{message,GELEN});
                    adptr.notifyDataSetChanged();
                }
            });
        }
    };
}
