package com.example.maceo.babylog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FeedingBottleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener  {

    Button mStartBottleFeedingBtn;
    EditText mDateAndTime;
    Button mSaveButton;
    private EditText mAmtInOZ;
    private EditText mBottleFeedingNotes;
    private FirebaseAuth mAuth;
    private String mDate;
    private String mTime;

    private int dayFinal, monthFinal, yearFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //database auth
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_bottle);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSaveButton = findViewById(R.id.save_Button);
        mAmtInOZ = findViewById(R.id.amt_in_oz);
        mBottleFeedingNotes = findViewById(R.id.bottle_feeding_notes_edt);
        mDateAndTime = findViewById(R.id.date_and_time);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountOfOz = mAmtInOZ.getText().toString();
                String bottleNotes = mBottleFeedingNotes.getText().toString();
                String dateAndTime = mDateAndTime.getText().toString();

                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user_id).child("Feeding").child("Bottle Feeding").child("Time Stamp");

                Map newPost = new HashMap();
                newPost.put("Amount_In_Oz", amountOfOz);
                newPost.put("Bottle_Feeding_Notes",bottleNotes);
                newPost.put("Date_and_Time",dateAndTime);

                current_user_db.child(mDate).child(mTime).setValue(newPost);

                Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });

        mStartBottleFeedingBtn = findViewById(R.id.start_bottle_feeding_btn);
        mDateAndTime = findViewById(R.id.date_and_time);

        mStartBottleFeedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FeedingBottleActivity.this, FeedingBottleActivity.this,
                        year,month,day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        yearFinal =year;
        monthFinal =month + 1;
        dayFinal =day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog =new TimePickerDialog(FeedingBottleActivity.this, FeedingBottleActivity.this,
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
        mDateAndTime.setText(monthFinal + "/"+ dayFinal + "/"+ yearFinal + " ("+ updateTime +")");

        mTime = " ("+ updateTime+")";
    }
    /*@Override
   *//* public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
}