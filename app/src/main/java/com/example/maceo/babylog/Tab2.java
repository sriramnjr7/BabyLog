package com.example.maceo.babylog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Tab2 extends Fragment {

    private TextView mLastBottleFeedingTimeStamp, mLastBFOZ,mLastBFNote;
    private TextView mLastBreastFeedTimeStamp, mLeftBreastFeed, mRightBreastFeed, mLastBreastFeedNote;
    private TextView mLastMealFeedTimeStamp, mLastMealConsumed, mLastSupplementConsumed, mLastMealNote;
    private TextView mLastDiaperTimeStamp, mLastDiaperStatus, mLastDiaperNote;
    private TextView mLastNapTimeStamp, mLastNapDuration, mLastNapNote;

    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3, valueEventListener4, valueEventListener5;

    private DatabaseReference current_user_db, current_user_db2, current_user_db3, current_user_db4, current_user_db5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        mLastBottleFeedingTimeStamp = view.findViewById(R.id.last_date_and_time);
        mLastBFOZ = view.findViewById(R.id.last_amount_in_oz);
        mLastBFNote = view.findViewById(R.id.txt_last_bf_note);
        mLastBreastFeedTimeStamp = view.findViewById(R.id.last_date_and_time_breast_feed);
        mLeftBreastFeed = view.findViewById(R.id.left_breast_feed_time);
        mRightBreastFeed = view.findViewById(R.id.right_breast_feed_time);
        mLastBreastFeedNote = view.findViewById(R.id.txt_note_breast_feed);
        mLastMealFeedTimeStamp = view.findViewById(R.id.last_time_stamp_meals);
        mLastMealConsumed = view.findViewById(R.id.last_meal_consumed);
        mLastSupplementConsumed = view.findViewById(R.id.last_supplement_consumed);
        mLastMealNote = view.findViewById(R.id.txt_note_meal);
        mLastDiaperTimeStamp = view.findViewById(R.id.diaper_last_timestamp);
        mLastDiaperStatus = view.findViewById(R.id.edt_diaper_status);
        mLastDiaperNote = view.findViewById(R.id.edt_diaper_note);
        mLastNapTimeStamp = view.findViewById(R.id.nap_last_timestamp);
        mLastNapDuration = view.findViewById(R.id.edt_nap_duration);
        mLastNapNote = view.findViewById(R.id.edt_nap_notes);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        current_user_db = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user_id)
                .child("Feeding").child("Bottle Feeding").child("Time Stamp");

        valueEventListener = current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();
                    //mLastBottleFeedingTimeStamp.setText(Date_and_Time);

                    current_user_db.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                //String Amount_In_Oz = child.getKey();
                                mLastBottleFeedingTimeStamp.setText(date +" "+time);
                                //mLastBFOZ.setText(Amount_In_Oz);
                                String Amount_In_Oz = child.child("Amount_In_Oz").getValue(String.class);
                                mLastBFOZ.setText(Amount_In_Oz);
                                String Bottle_Feeding_Notes = child.child("Bottle_Feeding_Notes").getValue(String.class);
                                mLastBFNote.setText(Bottle_Feeding_Notes);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // end of bottle feeding

        /*
        ***********************************************************************************************************
         */

        // beginning of brest feeding
        current_user_db2 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Breast Feeding").child("Time Stamp");

        valueEventListener2 = current_user_db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db2.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                mLastBreastFeedTimeStamp.setText(date + " "+ time);
                                String Left_Breast_Feeding_Time = child.child("Left_Breast_Feeding_Time").getValue(String.class);
                                mLeftBreastFeed.setText(Left_Breast_Feeding_Time);
                                String Right_Breast_Feeding_Time = child.child("Right_Breast_Feeding_Time").getValue(String.class);
                                mRightBreastFeed.setText(Right_Breast_Feeding_Time);
                                String Breast_Feeding_Note = child.child("Breast_Feeding_Note").getValue(String.class);
                                mLastBreastFeedNote.setText(Breast_Feeding_Note);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //end of breast feeding

        /*
        ***********************************************************************************************************
         */

        // beginning of meal feeding
        current_user_db3 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Meal Feeding").child("Time Stamp");

       valueEventListener3 = current_user_db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db3.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                mLastMealFeedTimeStamp.setText(date + " "+ time);
                                String Meal_Consumed = child.child("Meal_Consumed").getValue(String.class);
                                mLastMealConsumed.setText(Meal_Consumed);
                                String Supplement_Consumed = child.child("Supplement_Consumed").getValue(String.class);
                                mLastSupplementConsumed.setText(Supplement_Consumed);
                                String Meal_Note = child.child("Meal_Note").getValue(String.class);
                                mLastMealNote.setText(Meal_Note);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //end of meal feeding

        /*
        ***********************************************************************************************************
         */

        // beginning of Diaper Status
        current_user_db4 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Diaper Status").child("Time Stamp");

        valueEventListener4 = current_user_db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db4.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String time = child.getKey();
                                mLastDiaperTimeStamp.setText(date + " " + time);
                                String last_diaper_status = child.child("last_diaper_status").getValue(String.class);
                                mLastDiaperStatus.setText(last_diaper_status);
                                String last_diaper_note = child.child("last_diaper_note").getValue(String.class);
                                mLastDiaperNote.setText(last_diaper_note);
                            }
                        }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        //end of Diaper Status
        /*
        ***********************************************************************************************************
         */

        // Beginning of Sleep Status

        current_user_db5 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Nap Entry").child("Time Stamp");

        valueEventListener5 = current_user_db5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                final String date = child.getKey();

                current_user_db5.child(date).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            String time = child.getKey();
                            mLastNapTimeStamp.setText(date + " " + time);
                            String lastNapDuration = child.child("sleep_time").getValue(String.class);
                            int lastNapDuration2 = Integer.parseInt(lastNapDuration);
                            if (lastNapDuration2 > 1){
                                mLastNapDuration.setText(lastNapDuration2 + " mins");
                            } else {
                                mLastNapDuration.setText(lastNapDuration + " min");
                            }
                            //mLastNapDuration.setText(lastNapDuration + " mins");
                            /*String lastNapNote = child.child("Nap_Notes").getValue(String.class);
                            mLastNapNote.setText(lastNapNote);*/
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // End of Sleep Status

        //end of on create method
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        current_user_db.removeEventListener(valueEventListener);
        current_user_db2.removeEventListener(valueEventListener2);
        current_user_db3.removeEventListener(valueEventListener3);
        current_user_db4.removeEventListener(valueEventListener4);
        current_user_db5.removeEventListener(valueEventListener5);
    }

}
