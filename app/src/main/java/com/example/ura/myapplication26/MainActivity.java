package com.example.ura.myapplication26;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import static com.example.ura.myapplication26.InitSocket.hasConnection;
import static com.example.ura.myapplication26.Variable.debug;
import static com.example.ura.myapplication26.Variable.head;
import static com.example.ura.myapplication26.Variable.imei;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    WebView tw1;
    Control control;
    Button bRest;
    @SuppressLint({"HandlerLeak", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Variable.imei = getImei();

        tw1 = findViewById(R.id.TW);
        WebSettings webSettings = tw1.getSettings();
        webSettings.setJavaScriptEnabled(true);

        bRest = (Button) findViewById(R.id.Rest); // определение кнопки test
        bRest.setOnClickListener(this);

        Log.e(debug, "msg -> " + imei);

        Variable.handtv = new Handler() {

            @SuppressLint("StringFormatInvalid")
            @Override
            public void handleMessage(Message msg) {
                switch (String.valueOf(msg.obj)) {
                    case "Откл":
                        bRest.setBackgroundColor(Color.RED);
                        break;
                    case "Вкл":
                        bRest.setBackgroundColor(Color.GREEN);
                    case "-":
                        //tv1("");
                       break;
                    default:
                        if(String.valueOf(msg.obj).startsWith("&*")) {
                            head =  String.valueOf(msg.obj).substring(2);
                            setTitle(Html.fromHtml("<small><b>" + String.format(getString(R.string.app_name), head) + "</font>"));
                        }
                        else
                            //tv1.setText(Html.fromHtml(String.valueOf(msg.obj)));
                            tw1.loadDataWithBaseURL(null, String.valueOf(msg.obj), "text/html", "UTF-8", null);
                          //  tv1.append(String.valueOf(msg.obj));
                        };
            }
        };
        control = new Control();
    }

    @SuppressLint("HardwareIds")
     String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) ?
              Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID):
              telephonyManager.getDeviceId();

    }
    protected void onStart() {
        super.onStart();

        control.SetCommand(new CallCommand(new Icommand(this)));
        control.press(R.id.Rest);
        setTitle(Html.fromHtml("<small><b>" + String.format(getString(R.string.app_name), head) + "</font>"));
        if (hasConnection(this))
            bRest.setBackgroundColor(Color.GREEN);
        else {
            bRest.setBackgroundColor(Color.RED);
          //  tv1.setText("Отсутсвует покдключение к сети.");
        }
         Log.e(debug, "onStart: " + hasConnection(this));
    }

    @Override
    public void onClick(View v) {
        control.SetCommand(new CallCommand(new Icommand(this)));
        control.press(v.getId());
    }
}
