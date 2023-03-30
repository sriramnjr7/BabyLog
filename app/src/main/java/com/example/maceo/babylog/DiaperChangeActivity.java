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

public class DiaperChangeActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    Button mStartDiaperChange;
    EditText mLastDiaperChangeTimeStamp, mDiaperNote;
    Button mDiaperSaveButton;
    private FirebaseAuth mAuth;
    private String mDate;
    private String mTime;
    private int dayFinal, monthFinal, yearFinal;

    private Spinner mDiaperStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_change);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        mDiaperSaveButton =(Button) findViewById(R.id.btn_diaper_save_status);

        mDiaperSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateAndTime = mLastDiaperChangeTimeStamp.getText().toString();
                String diaperStatus = mDiaperStatusSpinner.getSelectedItem().toString();
                String diaperNote = mDiaperNote.getText().toString();

                //spinner for diaper status
                mDiaperStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String diaper_status = adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                //database stuff here
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(user_id).child("Diaper Status").child("Time Stamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("last_diaper_status", diaperStatus);
                newPost.put("last_diaper_note",diaperNote);
                newPost.put("Date_and_Time",dateAndTime);

                current_user_db.child(mDate).child(mTime).setValue(newPost);
                //end of database stuff


                Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });

        mStartDiaperChange =(Button) findViewById(R.id.btn_start_diaper_change);
        mLastDiaperChangeTimeStamp =(EditText) findViewById(R.id.edt_last_diaper_change_timestamp);
        mDiaperNote = (EditText) findViewById(R.id.edt_diaper_note);

        mStartDiaperChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day =c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DiaperChangeActivity.this, DiaperChangeActivity.this,
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

        TimePickerDialog timePickerDialog =new TimePickerDialog(DiaperChangeActivity.this, DiaperChangeActivity.this,
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
        mLastDiaperChangeTimeStamp.setText(monthFinal + "/"+ dayFinal + "/"+ yearFinal + " ("+ updateTime +")");

        mTime = " ("+ updateTime+")";
    }

    public void addListenerOnSpinnerItemSelection() {
        mDiaperStatusSpinner = (Spinner) findViewById(R.id.sp_diaper_status);
        //mDiaperStatusSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    // get the selected dropdown list value
    public void addListenerOnButton() {

        mDiaperStatusSpinner = (Spinner) findViewById(R.id.sp_diaper_status);

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
