package com.example.maceo.babylog;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class AddNotification extends DialogFragment {
    private Button mCancel, mSave, mTimeOk, mDateOk;
    private TextInputEditText mAddTitle;
    private TextView mDate, mTime;
    private CalendarView calendarView;
    private TimePicker timePicker;
    private String saveDate;
    private String saveTime;
    private long inMillis;
    private int global_year,  global_month, global_dayOfMonth;

    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.add_notification, container, false);
        getDialog().setTitle("Add New Reminder");

        mAuth = FirebaseAuth.getInstance();

        mCancel = view.findViewById(R.id.cancel);
        mTimeOk = view.findViewById(R.id.time_ok);
        mDateOk = view.findViewById(R.id.date_ok);
        mSave = view.findViewById(R.id.save);
        mDate = view.findViewById(R.id.date);
        mTime = view.findViewById(R.id.time);
        mAddTitle = view.findViewById(R.id.add_title);
        calendarView = view.findViewById(R.id.calender);
        calendarView.setVisibility(View.GONE);
        timePicker = view.findViewById(R.id.timePicker);
        timePicker.setVisibility(View.GONE);
        mTimeOk.setVisibility(View.GONE);
        mDateOk.setVisibility(View.GONE);
        final Calendar calendar = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                global_year = year;
                global_month = month + 1;
                global_dayOfMonth = dayOfMonth;

                mDateOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveDate = String.valueOf(global_month) + "/" + String.valueOf(global_dayOfMonth) + "/" + String.valueOf(global_year);
                        mDate.setText("Date: " + saveDate);
                        calendarView.setVisibility(View.GONE);
                        mDateOk.setVisibility(View.GONE);
                    }
                });
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);


                mTimeOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
                        mTime.setText("Time: " +  saveTime);
                        timePicker.setVisibility(View.GONE);
                        mTimeOk.setVisibility(View.GONE);
                    }
                });
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(calendarView.isShown()
                ?View.GONE
                :View.VISIBLE);
                mDateOk.setVisibility(mDateOk.isShown()
                        ?View.GONE
                        :View.VISIBLE);
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(timePicker.isShown()
                        ?View.GONE
                        :View.VISIBLE);
                mTimeOk.setVisibility(mTimeOk.isShown()
                        ?View.GONE
                        :View.VISIBLE);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked Cancel", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mAuth.getCurrentUser().getUid();
                String time_millis = String.valueOf(System.currentTimeMillis());
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(user_id).child("Notifications").child(time_millis);

                String title = mAddTitle.getText().toString();
                if(calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }
                inMillis = calendar.getTimeInMillis();
                ListItem listItem = new ListItem(title, saveDate, saveTime, inMillis, true);

                current_user_db.setValue(listItem);
                long id = Long.valueOf(time_millis);
                startAlarm((int) id, inMillis, saveTime, title);

                dismiss();
            }
        });

        return view;
    }

    private void startAlarm(int id, long time, String body, String title) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
        if(diff > 0){
            calendar.add(Calendar.DATE, 1);
        }
        time = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("time", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }
}
