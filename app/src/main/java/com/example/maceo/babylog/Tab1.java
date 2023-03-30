package com.example.maceo.babylog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Tab1 extends Fragment {
    private ImageView baby_pic;
    private TextView mDisplayBabyName, mDisplayBabyBirthAge, mDisplayBabyWeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");


        Button mFeedingButton = view.findViewById(R.id.feeding_button);
        Button mSleepButton = view.findViewById(R.id.sleep_button);
        Button mDiaperChangeButton = view.findViewById(R.id.diaper_change_button);
        //Button mChartButton = view.findViewById(R.id.chart_button);
        Button mTummyTime = view.findViewById(R.id.tummytime_button);
        Button mWeightTracking = view.findViewById(R.id.weight_tracker_button);
        baby_pic = view.findViewById(R.id.imageView3);
        mDisplayBabyName = view.findViewById(R.id.txt_view_baby_name);
        mDisplayBabyBirthAge = view.findViewById(R.id.txt_view_birthday);
        mDisplayBabyWeight = view.findViewById(R.id.txt_view_weight);
        Button mMemoriesBtn = view.findViewById(R.id.btn_memories);

        DatabaseReference current_user_pic = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("profile_pic");
        current_user_pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.getValue(String.class);
                Picasso.get().load(imageUrl).into(baby_pic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getView().getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mFeedingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(),FeedingActivity.class);
                startActivity(x);
            }
        });

        mMemoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(),MemoriesActivity.class);
                startActivity(x);
            }
        });

        mSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(),SleepActivity.class);
                startActivity(x);
            }
        });

        mDiaperChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(),DiaperChangeActivity.class);
                startActivity(x);
            }
        });

        /*mChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(),ChartActivity.class);
                startActivity(x);
            }
        });*/
        mWeightTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getActivity(), WeighyTrackingActivity.class);
                startActivity(x);
                                            }
                                        });
        mTummyTime.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* In here put the java class you want it to go to*/
                Intent x = new Intent(getActivity(),TummyTimeActivity.class);
                startActivity(x);
            }
        }));


        // __________________RETRIEVE BABY NAME_________________________________

        DatabaseReference current_user_name = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("name");
        current_user_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                mDisplayBabyName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // __________________RETRIEVE BABY NAME_________________________________

        // __________________RETRIEVE BABY WEIGHT_________________________________

        DatabaseReference current_user_name4 = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("weight");
        current_user_name4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String weight = dataSnapshot.getValue(String.class);
                mDisplayBabyWeight.setText(weight+"  lbs");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // __________________RETRIEVE BABY WEIGHT_________________________________


        // __________________RETRIEVE BABY BIRTHDATE FOR AGE_________________________________

        DatabaseReference current_user_name2 = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("birthday");
        current_user_name2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar cal = Calendar.getInstance();

                String birthday = dataSnapshot.getValue(String.class);
                String[] output = birthday.split("\\/");
                int mMonthBorn = Integer.parseInt(output[0]);
                int mDayBorn = Integer.parseInt(output[1]);
                int mYearBorn = Integer.parseInt(output[2]);

                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();

                dob.set(mYearBorn, mMonthBorn-1, mDayBorn);

                int years = today.get(Calendar.YEAR) - dob.get((Calendar.YEAR));
                int months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);

                if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
                    months--;
                }
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    years--;
                }

                if(years != 0){
                    Integer monthsInt = months;
                    Integer yearsInt = years;
                    String ageS =yearsInt.toString() + " year " + monthsInt.toString() + " months old";
                    mDisplayBabyBirthAge.setText(ageS);


                }else {
                    Integer monthsInt = months;
                    String ageS = monthsInt.toString() + " months old";

                    mDisplayBabyBirthAge.setText(ageS);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // __________________RETRIEVE BABY BIRTHDATE FOR AGE_________________________________




        return view;
    }
}
