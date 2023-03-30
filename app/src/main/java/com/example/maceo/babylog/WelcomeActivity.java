package com.example.maceo.babylog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class WelcomeActivity extends AppCompatActivity {

    // TODO: Declare member variables here:
    ImageButton mBoyButton;
    ImageButton mGirlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // TODO: casting we converted a view object to a button object
        mBoyButton = (ImageButton) findViewById(R.id.boy_button);
        mGirlButton = (ImageButton) findViewById(R.id.girl_button);


        // TODO: set OnClick listener to change screen when button is pressed

        mBoyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent r = new Intent(WelcomeActivity.this,BabyInfoActivity.class);
                startActivity(r);
            }
        });

        mGirlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent r = new Intent(WelcomeActivity.this,BabyInfoActivity.class);
                startActivity(r);
            }
        });

    }
}
