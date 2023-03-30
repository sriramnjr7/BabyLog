package com.example.maceo.babylog;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.stream.*;


import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;


public class Tab3 extends Fragment {

    LineChart mBottleFeedingChart;
    LineChart mBreastFeedingChart;
    BarChart mSleepingchart;
    PieChart mDiaperChangeChart;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        // __________________START FOR CREATING BABY BOTTLE GRAPH_________________________________
        mBottleFeedingChart = (LineChart) view.findViewById(R.id.line_chart_bottle_feeding);
        // __________________START FOR Retrieving database(bottle time and amount in OZ) information_________________________________

        final DatabaseReference current_user_db4 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Bottle Feeding").child("Time Stamp");

        current_user_db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db4.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String[] xValues={};
                            float[] yValues={};

                            // for loop to iterate timestamps of all database records
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String time2 = snapshot.getKey();

                                int currentSize = xValues.length;
                                int newSize = currentSize+1;
                                String[] tempArray = new String[newSize];
                                for (int i=0; i<currentSize; i++){
                                    tempArray[i] = xValues[i];
                                }
                                tempArray[newSize-1] = time2;
                                xValues = tempArray;

                            }

                            // for loop to iterate all amount in OZ recorded on database
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String Amount_In_Oz2 = child.child("Amount_In_Oz").getValue(String.class);
                                float Amount_In_Oz2_f = Float.parseFloat(Amount_In_Oz2);

                                int currentSize = yValues.length;
                                int newSize = currentSize+1;
                                float[] tempArray = new float[newSize];
                                for (int i=0; i<currentSize; i++){
                                    tempArray[i] = yValues[i];
                                }
                                tempArray[newSize-1] = Amount_In_Oz2_f;
                                yValues = tempArray;

                            }

                            //here we can change graph features and settings
                            XAxis xAxis = mBottleFeedingChart.getXAxis();
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            drawLineGraph(yValues,xValues);

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

        // __________________ENDING FOR Retrieving database(bottle time and amount in OZ) information_________________________________

        // __________________ENDING FOR CREATING BABY BOTTLE GRAPH_________________________________




        // __________________START FOR CREATING BREASTFEEDING GRAPH_________________________________
        mBreastFeedingChart = (LineChart) view.findViewById(R.id.line_chart_breast_feeding);


        final DatabaseReference current_user_db5 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Breast Feeding").child("Time Stamp");

        current_user_db5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db5.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //String time1 = "0";
                            String[] xValues = {};
                            //float lFeedingDuration = 0;
                            float[] yValues = {};
                            //float rFeedingDuration = 0;
                            float[] yValues2 = {};

                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                String time2 = snapshot.getKey();

                                int currentSize = xValues.length;
                                int newSize = currentSize + 1;
                                String[] tempArray = new String[newSize];
                                for(int i = 0; i < currentSize; i++)
                                {
                                    tempArray[i] = xValues[i];
                                }
                                tempArray[newSize - 1] = time2;
                                xValues = tempArray;
                            }

                            for(DataSnapshot child: dataSnapshot.getChildren()) {
                                String lBreastFeedingDuration = child.child("Left_Breast_Feeding_Time").getValue(String.class);
                                float lBreastDuration = Float.parseFloat(lBreastFeedingDuration);
                                String rBreastFeedingDuration = child.child("Right_Breast_Feeding_Time").getValue(String.class);
                                float rBreastDuration = Float.parseFloat(rBreastFeedingDuration);

                                int currentSize = yValues.length;
                                int newSize = currentSize + 1;
                                float[] tempArray = new float[newSize];
                                float[] tempArray2 = new float[newSize];
                                for (int i = 0; i < currentSize; i++) {
                                    tempArray[i] = yValues[i];
                                    tempArray2[i] = yValues2[i];
                                }
                                tempArray[newSize - 1] = lBreastDuration;
                                tempArray2[newSize - 1] = rBreastDuration;
                                yValues = tempArray;
                                yValues2 = tempArray2;
                            }

                            XAxis xAxis = mBreastFeedingChart.getXAxis();
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            drawMultiLineGraph(yValues, yValues2, xValues);
                            //drawLineGraph(yValues, xValues);
                            //drawLineGraph(yValues2, xValues);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });


        // __________________ENDING FOR CREATING BREASTFEEDING GRAPH_________________________________


        // __________________START FOR CREATING Sleeping GRAPH_________________________________
        mSleepingchart = (BarChart) view.findViewById(R.id.bar_chart_sleeping);

        // __________________START FOR Retrieving database(amount of sleep) information_________________________________

        final DatabaseReference current_user_db2 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Nap Entry").child("Time Stamp");

        current_user_db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db2.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String[] xValues_bargraph={};
                            int[] yValues_bargraph={};

                            // for loop to iterate timestamps of all database records
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String time2 = snapshot.getKey();

                                int currentSize = xValues_bargraph.length;
                                int newSize = currentSize+1;
                                String[] tempArray = new String[newSize];
                                for (int i=0; i<currentSize; i++){
                                    tempArray[i] = xValues_bargraph[i];
                                }
                                tempArray[newSize-1] = time2;
                                xValues_bargraph = tempArray;

                            }



                            // for loop to iterate all amount in OZ recorded on database
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String sleep_time2 = child.child("sleep_time").getValue(String.class);
                                int sleep_time2_f = Integer.parseInt(sleep_time2);

                                int currentSize = yValues_bargraph.length;
                                int newSize = currentSize+1;
                                int[] tempArray = new int[newSize];
                                for (int i=0; i<currentSize; i++){
                                    tempArray[i] = yValues_bargraph[i];
                                }
                                tempArray[newSize-1] = sleep_time2_f;
                                yValues_bargraph = tempArray;

                            }

                            //here we can change graph features and settings
                            XAxis xAxis = mSleepingchart.getXAxis();
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            drawBarGraph(yValues_bargraph,xValues_bargraph);

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

        // __________________ENDING FOR Retrieving database(amount of sleep) information_________________________________

        // __________________ENDING FOR CREATING BABY Sleeping GRAPH_________________________________



        // __________________START FOR CREATING DIAPER CHANGE GRAPH_________________________________
        mDiaperChangeChart = (PieChart) view.findViewById(R.id.pie_chart_diaper_change);

        // __________________START FOR Retrieving database(DIAPER CHANGE) information_________________________________

        final DatabaseReference current_user_db3 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Diaper Status").child("Time Stamp");

        current_user_db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db3.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String[] yValues_piegraph={};
                            int count_wet_diaper = 0;
                            int count_dry_diaper = 0;
                            int count_wet_diaper2 = 0;
                            int count_dry_diaper2 = 0;
                            //String diaper_stat2 = "" ;

                            // for loop to iterate all amount in OZ recorded on database
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String diaper_stat = child.child("last_diaper_status").getValue(String.class);

                                //

                                if (diaper_stat.equals("WET")){
                                    count_wet_diaper2++;
                                } else if (diaper_stat.equals("DRY")) {
                                    count_dry_diaper2++;
                                }


                                //

                                int currentSize = yValues_piegraph.length; //correct
                                int newSize = currentSize+1; //correct
                                String[] tempArray = new String[newSize]; //correct
                                for (int i=0; i<currentSize; i++){
                                    tempArray[i] = yValues_piegraph[i];
                                }
                                tempArray[newSize-1] = diaper_stat;
                                yValues_piegraph = tempArray;


                            }

                            count_dry_diaper = count_dry_diaper2 + count_dry_diaper;
                            count_wet_diaper = count_wet_diaper2 + count_wet_diaper;


                        mDiaperChangeChart.setUsePercentValues(true);
                        mDiaperChangeChart.getDescription().setEnabled(false);
                        mDiaperChangeChart.setExtraOffsets(5,10,5,5);

                        mDiaperChangeChart.setDragDecelerationFrictionCoef(0.15f);

                        mDiaperChangeChart.setDrawHoleEnabled(true);
                        mDiaperChangeChart.setHoleColor(Color.WHITE);
                        mDiaperChangeChart.setTransparentCircleRadius(50);

                        ArrayList<PieEntry> yValues_pieChart = new ArrayList<>();
                        yValues_pieChart.add(new PieEntry(count_wet_diaper,"Wet"));
                        yValues_pieChart.add(new PieEntry(count_dry_diaper,"Dry"));

                        PieDataSet dataSet = new PieDataSet(yValues_pieChart,"Percentages");
                        dataSet.setValueTextColor(Color.BLACK);
                        dataSet.setSliceSpace(3f);
                        dataSet.setDrawValues(true);
                        dataSet.setSelectionShift(5f);
                        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                        PieData data = new PieData((dataSet));
                        data.setValueTextSize(20f);
                        data.setDrawValues(true);
                        data.setValueTextColor(Color.BLACK);

                        mDiaperChangeChart.setData(data);
                        mDiaperChangeChart.animateY(6000);



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

        // __________________ENDING FOR Retrieving database(DIAPER CHANGE) information_________________________________

        // __________________ENDING FOR CREATING BABY DIAPER CHANGE GRAPH_________________________________










        // end of on create method.
        return view;
    }


    // __________________START FOR CREATING DIAPER GRAPH_________________________________


    // __________________END FOR CREATING DIAPER GRAPH_________________________________



    // __________________START FOR CREATING BABY SLEEP GRAPH_________________________________

    private void drawBarGraph(int [] yValues_bargraph, String [] xValues_bargraph){
        List<BarEntry> yData = new ArrayList<>();
        for (int i = 0; i<yValues_bargraph.length;i++){
            yData.add(new BarEntry(i,yValues_bargraph[i]));
        }

        BarDataSet set2;
        set2 = new BarDataSet(yData,"Sleep Time in Mins");
        set2.setColors(ColorTemplate.MATERIAL_COLORS);
        set2.setDrawValues(true);
        set2.setValueTextSize(30f);
        BarData data = new BarData(set2);
//        IAxisValueFormatter xAxisFormatter = new LabelFormatter(xValues_bargraph);
        XAxis xAxis = mSleepingchart.getXAxis();
//        xAxis.setValueFormatter(xAxisFormatter);
        mSleepingchart.setData(data);
        mSleepingchart.invalidate();

        mSleepingchart.animateY(5000);

    }

    // __________________END FOR CREATING BABY SLEEP GRAPH_________________________________



    // __________________START FOR CREATING BABY BOTTLE GRAPH_________________________________

    private void drawLineGraph(float [] yValues, String [] xValues){
        ArrayList<Entry> yData = new ArrayList<>();
        for (int i = 0; i < yValues.length; i++){
            yData.add(new Entry(i,yValues[i]));
        }

        LineDataSet set1;
        set1 = new LineDataSet(yData,"Bottle Feeding In OZ");
        set1.setColor(Color.BLUE);
        set1.setDrawValues(true);
        set1.setValueTextSize(20f);
        LineData data = new LineData(set1);
//       mBottleFeedingChart.getXAxis().setValueFormatter(new LabelFormatter(xValues));
        mBottleFeedingChart.setData(data);
        mBottleFeedingChart.invalidate();
        mBottleFeedingChart.animateY(2000);

    }
    
    private void drawMultiLineGraph(float[] yValues, float[] yValues2, String[] xValues){
        ArrayList<Entry> yData = new ArrayList<>();
        ArrayList<Entry> yData2 = new ArrayList<>();
        for(int i = 0; i < yValues.length; i++)
        {
            yData.add(new Entry(i, yValues[i]));
            yData2.add(new Entry(i, yValues2[i]));
        }
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet setl, setr;
        setl = new LineDataSet(yData, "Left Breast Feeding Time in Mins");
        setl.setColor(Color.MAGENTA);
        setr = new LineDataSet(yData2, "Right Breast Feeding Time in Mins");
        setr.setColor(Color.RED);
        setl.setValueTextSize(20f);
        setr.setValueTextSize(20f);
        lineDataSets.add(setl);
        lineDataSets.add(setr);
        LineData data = new LineData(lineDataSets);
        LineData data2 = new LineData(setr);
//        mBreastFeedingChart.getXAxis().setValueFormatter(new LabelFormatter(xValues));
        mBreastFeedingChart.setData(data);
        mBreastFeedingChart.invalidate();
        mBreastFeedingChart.animateY(2000);
        //mBreastFeedingChart.setData(data2);
    }

//    public class LabelFormatter implements IAxisValueFormatter {
//        private final String[] mLabels;
//
//        public LabelFormatter(String[] labels) {
//            mLabels = labels;
//        }
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//
//            return mLabels[(int) value];
//        }
//    }

    // __________________ENDING FOR CREATING BABY BOTTLE GRAPH_________________________________








    // ending of tab3 file
}
