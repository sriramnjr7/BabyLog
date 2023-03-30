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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BreastFeedingActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private Button mStartBreastFeedingBTN;
    private EditText mDateAndTimeResult;
    private Button mSaveBtn;
    private EditText mBreastFeedingNotes;
    private Spinner leftBreastFeedSpinner, rightBreastFeedSpinner;
    //    int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal;
    private FirebaseAuth mAuth;
    private String uniqueBreastFeedingID;
    private String mDate;
    private String mTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //database auth
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_feeding);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();


        mSaveBtn =(Button) findViewById(R.id.save_Button);
        mStartBreastFeedingBTN =(Button) findViewById(R.id.start_breast_feeding_btn);
        mDateAndTimeResult =(EditText) findViewById(R.id.date_and_time_breastFeeding);
        leftBreastFeedSpinner = (Spinner) findViewById(R.id.left_breast_feed_spinner);
        rightBreastFeedSpinner = (Spinner) findViewById(R.id.right_breast_feed_spinner);
        mBreastFeedingNotes = (EditText) findViewById(R.id.breast_feeding_notes);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateAndTime = mDateAndTimeResult.getText().toString();
                String breastFeedingNote = mBreastFeedingNotes.getText().toString();
                String leftBreastFeedingTime = leftBreastFeedSpinner.getSelectedItem().toString();
                String rightBreastFeedingTime = rightBreastFeedSpinner.getSelectedItem().toString();

                //spinner for left breast feeding
                leftBreastFeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String left_time = adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //spinner for right breast feeding
                rightBreastFeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String right_time = adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                //database stuff here
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user_id).child("Feeding").child("Breast Feeding").child("Time Stamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("Left_Breast_Feeding_Time", leftBreastFeedingTime);
                newPost.put("Right_Breast_Feeding_Time", rightBreastFeedingTime);
                newPost.put("Breast_Feeding_Note",breastFeedingNote);
                newPost.put("Date_and_Time",dateAndTime);

                current_user_db.child(mDate).child(mTime).setValue(newPost);

                Intent i =new Intent(getApplicationContext(),FeedingActivity.class);
                startActivity(i);
            }
        });

        mStartBreastFeedingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BreastFeedingActivity.this, BreastFeedingActivity.this,
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

        TimePickerDialog timePickerDialog =new TimePickerDialog(BreastFeedingActivity.this, BreastFeedingActivity.this,
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
        mDateAndTimeResult.setText(monthFinal + "/"+ dayFinal + "/"+ yearFinal + " ("+ updateTime+")");

        mTime = " ("+ updateTime+")";
    }
    // add items into spinner dynamically
    public void addItemsOnSpinner2() {
        rightBreastFeedSpinner = findViewById(R.id.right_breast_feed_spinner);
        //rightBreastFeedSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    public void addListenerOnSpinnerItemSelection() {
        leftBreastFeedSpinner = findViewById(R.id.left_breast_feed_spinner);
        //leftBreastFeedSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    // get the selected dropdown list value
    public void addListenerOnButton() {

        leftBreastFeedSpinner = findViewById(R.id.left_breast_feed_spinner);
        rightBreastFeedSpinner = findViewById(R.id.right_breast_feed_spinner);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            /*startActivity(new Intent(this, HomeActivity.class));
            return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
}