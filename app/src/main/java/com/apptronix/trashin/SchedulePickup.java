package com.apptronix.trashin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.Manifest;
import android.widget.Toast;

public class SchedulePickup extends AppCompatActivity {

    TimePicker timePicker;
    SmsManager smsManager;
    double lat,lng;
    Button button;
    String vehicle=" Vehicle:Ace", location, time;
    RadioButton rad;
    int hour,min;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_pickup);

        timePicker=(TimePicker)findViewById(R.id.timePicker);
        button=(Button)findViewById(R.id.button2);
        rad=(RadioButton)findViewById(R.id.button22);

        intent = getIntent();
        lat=intent.getDoubleExtra("lat",0);
        lng=intent.getDoubleExtra("lng",0);
        location=" Location:"+String.valueOf(lat)+","+String.valueOf(lng);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 123);
            button.setEnabled(false);
            Toast.makeText(getApplicationContext(),"Please grant Trashin permission to send SMS from Settings",Toast.LENGTH_LONG).show();
        } else {
            button.setEnabled(true);
        }

    }

    public void sendSMS(View v){

        if(rad.isChecked())
            vehicle=" Vehicle:407";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour=timePicker.getHour();
            min=timePicker.getMinute();
        }else {
            hour=timePicker.getCurrentHour();
            min=timePicker.getCurrentMinute();
        }
        time=" Time: "+String.valueOf(hour)+":"+String.valueOf(min);

        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                "+919632465714",
                null,
                time+vehicle+location,
                null,
                null
        );

        intent = new Intent(this, MainActivity.class);
        new AlertDialog.Builder(this)
                .setTitle("Thank You")
                .setMessage("Your pickup has been scheduled!")
                .setPositiveButton("Book Another Pickup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(R.drawable.trashin)
                .show();


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                button.setEnabled(true);
            } else {

            }
        }
    }
}

