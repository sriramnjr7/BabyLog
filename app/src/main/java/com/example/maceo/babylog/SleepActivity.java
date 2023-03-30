package com.example.maceo.babylog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepActivity extends AppCompatActivity
        implements /*NavigationView.OnNavigationItemSelectedListener,*/
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    ImageButton mStartSleepButton;
    Button mSleepSaveButton;
    EditText mLastSleepTimeStamp;
    TextView mSleepTime;
    NumberPicker noPicker;
    private FirebaseAuth mAuth;
    private String mDate;
    private String mTime;
    private int dayFinal, monthFinal, yearFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        //Start code here
        mAuth = FirebaseAuth.getInstance();
        mStartSleepButton =(ImageButton) findViewById(R.id.start_sleep_timestamp_btn);
        mSleepSaveButton = (Button) findViewById(R.id.btn_save_sleep);
        mLastSleepTimeStamp = (EditText) findViewById(R.id.edt_sleep_time_stamp);
        mSleepTime = (TextView) findViewById(R.id.txt_sleep_time_view);
        noPicker = findViewById(R.id.number_picker_sleep);

        noPicker.setMaxValue(60);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(true);

        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mSleepTime.setText(String.valueOf(newVal));
            }
        });

        mStartSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SleepActivity.this, SleepActivity.this,
                        year,month,day);
                datePickerDialog.show();

            }
        });


        mSleepSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateAndTime = mLastSleepTimeStamp.getText().toString();
                String sleep_time = mSleepTime.getText().toString();
                //database stuff here
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user_id).child("Nap Entry").child("Time Stamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                //newPost.put("last_diaper_status", diaperStatus);
                newPost.put("sleep_time",sleep_time);
                newPost.put("Date_and_Time",dateAndTime);

                current_user_db.child(mDate).child(mTime).setValue(newPost);
                //end of database stuff


                Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });

        /*NumberPicker noPicker;
        final TextView lb_w2;*/
        /*lb_w2 = findViewById(R.id.txt_sleep_time_view);
        noPicker = findViewById(R.id.number_picker_sleep);
        noPicker.setMaxValue(60);
        noPicker.setMinValue(1);
        noPicker.setWrapSelectorWheel(true);

        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                lb_w2.setText(String.valueOf(newVal));
            }
        });*/

        //end code here

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.myDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    } // end of oncreae


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        yearFinal =year;
        monthFinal =month + 1;
        dayFinal =day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog =new TimePickerDialog(SleepActivity.this, SleepActivity.this,
                hour,minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

        mDate = monthFinal + "-" + dayFinal + "-" + yearFinal;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        String updateTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        mLastSleepTimeStamp.setText(monthFinal + "/"+ dayFinal + "/"+ yearFinal + " ("+ updateTime +")");

        mTime = " ("+ updateTime+")";
    }







}