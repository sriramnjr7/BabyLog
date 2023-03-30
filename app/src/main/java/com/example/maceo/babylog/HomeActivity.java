package com.example.maceo.babylog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

//import com.bumptech.glide.Glide;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    // TODO: Declare member variables here:
/*    NotificationBadge mBadge;
    private int count = 0;*/
    // for FAB
    FloatingActionMenu floatingActionMenu;
    com.github.clans.fab.FloatingActionButton camera, sleep, feeding;
    // for fragment pages
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    // for camera
    private static final int CAM_REQUEST = 1313;
    private ImageView imgTakenPic;
    private TextView babyName;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        navigationView = findViewById(R.id.nav_view);
        babyName =  navigationView.getHeaderView(0).findViewById(R.id.babyName);
        imgTakenPic =  navigationView.getHeaderView(0).findViewById(R.id.profilePicture);

        DatabaseReference current_user_name = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("name");
        current_user_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                babyName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference current_user_pic = mDatabaseRef.child(mAuth.getCurrentUser().getUid()).child("Info").child("profile_pic");
        current_user_pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.getValue(String.class);
                Picasso.get().load(imageUrl).into(imgTakenPic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        mBadge = (NotificationBadge)findViewById(R.id.notification_badge);

        // finds FAB menu
        floatingActionMenu = findViewById(R.id.floatingActionMenu);
        camera = findViewById(R.id.fab_camera);
        sleep = findViewById(R.id.fab_sleep);
        feeding = findViewById(R.id.fab_food);

        /* start of individual button implementation */
        feeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.toggle(true);
                Intent intent= new Intent(HomeActivity.this,FeedingActivity.class);
                startActivity(intent);
            }
        });
        camera.setOnClickListener(new btnTakePhotoClicker());
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.toggle(false);
                Intent intent= new Intent(HomeActivity.this,SleepActivity.class);
                startActivity(intent);
            }
        });
        /* end of individual button implementation */

        // sets up PagerAdapter Class
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        // Setup the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.pager);
        setupViewPager(mViewPager);

        /* start of the tab layout and navigation view */
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout myDrawer = findViewById(R.id.myDrawer);
        ActionBarDrawerToggle myToggle = new ActionBarDrawerToggle(this,myDrawer,toolbar,R.string.open,R.string.close);

        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /* start of the tab layout and navigation view */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // no inspection SimplifiableIfStatement
        if (id == R.id.menu_notification) {
            Intent intent= new Intent(this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "Home");
        adapter.addFragment(new Tab2(), "Summary");
        adapter.addFragment(new Tab3(), "Chart");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.myDrawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id){
            case R.id.home:
                /*Intent intent = new Intent(this,HomeActivity.class);
                startActivity(intent);*/
                break;
            case R.id.feed:
                Intent h= new Intent(this,FeedingActivity.class);
                startActivity(h);
                break;
            case R.id.sleep:
                Intent i= new Intent(this,SleepActivity.class);
                startActivity(i);
                break;
            case R.id.diaper:
                Intent g= new Intent(this,DiaperChangeActivity.class);
                startActivity(g);
                break;
            case R.id.chart:
                Intent s= new Intent(this,ChartActivity.class);
                startActivity(s);
                break;
            case R.id.journal:
                /*Intent t= new Intent(this,.class);
                startActivity(t);*/
                break;
            case R.id.tt:
                Intent tt = new Intent(this,TummyTimeActivity.class);
                startActivity(tt);
                break;
            case R.id.weightT:
                Intent t= new Intent(this,WeighyTrackingActivity.class);
                startActivity(t);
                break;
            case R.id.editProfile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.myDrawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Do Not delete
    // this will show picture on the app but taken off for now
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imgTakenPic.setImageBitmap(bitmap);
        }
    }*/

    // sends request to take pic
    class btnTakePhotoClicker implements com.github.clans.fab.FloatingActionButton.OnClickListener{
        @Override
        public void onClick(View view){
            floatingActionMenu.toggle(false);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
    }
}
