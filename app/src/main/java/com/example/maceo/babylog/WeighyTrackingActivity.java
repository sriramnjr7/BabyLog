package com.example.maceo.babylog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class WeighyTrackingActivity extends AppCompatActivity {


    private ImageButton mImageButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mBabyBirthDate;
    private TextView EditWeight;
    int import_weight = 0;
    private FirebaseAuth mAuth;



    Button savebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weighy_tracking);
        mImageButton = (ImageButton) findViewById(R.id.cal_button);
        mBabyBirthDate = (EditText)findViewById(R.id.baby_birthday);
        savebutton =(Button) findViewById(R.id.savebutton);
        EditWeight = (TextView)findViewById(R.id.baby_weight_changeweight) ;
        mAuth = FirebaseAuth.getInstance();

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        WeighyTrackingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        // number picker thing
        NumberPicker noPicker;
        final TextView lb_w;
        lb_w = findViewById(R.id.baby_weight_changeweight);
        noPicker = findViewById(R.id.np);
        noPicker.setMaxValue(100);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(true);


        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                lb_w.setText(String.valueOf(newVal));
                import_weight = newVal;
            }
        });




        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                mBabyBirthDate.setText(date);
            }
        };



        String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Users/"+user_id+"/Info/weight");


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String weight = Integer.toString(import_weight);
                weight = EditWeight.getText().toString();
                if(weight.isEmpty()){
                    EditWeight.setError("Weight Required");
                    EditWeight.requestFocus();
                    return;
                }

                current_user_db.setValue(weight).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(getView().getContext(), "Name Successfull Changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getView().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });



    }
}
