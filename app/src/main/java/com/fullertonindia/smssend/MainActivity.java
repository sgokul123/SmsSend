package com.fullertonindia.smssend;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button sendSMSBtn;
    EditText toPhoneNumberET;
    EditText smsMessageET;

    int subscribe=0;
    AppCompatButton sim1,sim2;
    private int simSub1,simSub2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendSMSBtn = (Button) findViewById(R.id.sendSMSBtn);
        toPhoneNumberET = (EditText) findViewById(R.id.toPhoneNumberET);
        sim1 = (AppCompatButton) findViewById(R.id.buttonSim1);
        sim2 = (AppCompatButton) findViewById(R.id.buttonSim2);
        smsMessageET = (EditText) findViewById(R.id.smsMessageET);
        sendSMSBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (subscribe==0){
                    sendSMS(simSub1);
                }else if (subscribe==1){
                    sendSMS(simSub2);
                }else {
                    sendSMS(simSub1);
                }
            }
        });

        sim1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                subscribe=0;
            }
        });

        sim2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                subscribe=1;
            }
        });
        getsimDetails();

    }

    protected void sendSMS(int subscribe) {
        String toPhoneNumber = toPhoneNumberET.getText().toString();
        String smsMessage = smsMessageET.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscribe);
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Sending SMS failed.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void getsimDetails(){
        try {
            String simTwoNumber = "     Sim 1\n ", simOneNumber = "Sim 2      \n";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager subManager = (SubscriptionManager)getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                List<SubscriptionInfo> subInfoList = null;
                subInfoList = subManager.getActiveSubscriptionInfoList();
                if (subInfoList != null && subInfoList.size() > 0) {
                    switch (subInfoList.size()) {
                        case 2:
                            simTwoNumber = (String) subInfoList.get(1).getDisplayName();
                            sim2.setText(simTwoNumber);
                            simSub2=subInfoList.get(1).getSubscriptionId();
                        case 1:
                            simOneNumber = (String) subInfoList.get(0).getDisplayName();
                            sim1.setText(simOneNumber);
                            simSub1=subInfoList.get(0).getSubscriptionId();
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   }


}
