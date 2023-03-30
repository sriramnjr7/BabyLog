package com.example.maceo.babylog;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BabyInfoActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mBabyWeight;
    private EditText mBabyName;
    private Spinner mBabyGender;
    private ProgressBar progressBar;
    private EditText mBabyBirthDate;

    private int STORAGE_PERMISSION_CODE = 1;

    private String babyImgURL;

    private ImageView mImageView;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;

    //declare firebase database
    // App 63 - part 2
    private StorageReference mStorageRef; //helps gets firebase intance
    private FirebaseAuth mAuth;
    private StorageTask mUploadTask;
    private Map newPost = new HashMap();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_info);

        mAuth = FirebaseAuth.getInstance();

        ImageButton mCalButton = findViewById(R.id.cal_button);
        mBabyName = findViewById(R.id.baby_name);
        mBabyBirthDate = findViewById(R.id.baby_birthday);
        mBabyWeight = findViewById(R.id.baby_weight);
        Button mSaveButton = findViewById(R.id.savebaby_info_button);
        TextView mTextView = findViewById(R.id.textView5);
        mBabyGender = findViewById(R.id.baby_gender);
        String babyInfo = "Enter your baby's information";

        progressBar = findViewById(R.id.progressbarIMG);

        mImageView = findViewById(R.id.add_pic);

        mTextView.setText(babyInfo);

//        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //this line below will create a child with name key and a value of 123
        //databaseReference.child("key").setValue("123");

        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BabyInfoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
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


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here we are declaring what the user types into the edit text
                String babyName = mBabyName.getText().toString();
                String babyBirthday = mBabyBirthDate.getText().toString();
                String babyWeight = mBabyWeight.getText().toString();
                String babyGender = mBabyGender.getSelectedItem().toString();

//                int babyWeightIntegerValue = 0;

                if(babyName.isEmpty()){
                    mBabyName.setError("Name required");
                    mBabyName.requestFocus();
                    return;
                }
                if(babyBirthday.isEmpty()){
                    mBabyBirthDate.setError("Baby birthday required");
                    mBabyBirthDate.requestFocus();
                    return;
                }
                if(babyWeight.isEmpty()){
                    mBabyWeight.setError("Weight required");
                    mBabyWeight.requestFocus();
                    return;
                }

                //spinner value and grabs the string selected from the Gender option
                mBabyGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         String babyGender = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                // we convert the string weight value into an integer for baby weight
                /*try {
                    babyWeightIntegerValue = Integer.parseInt(babyWeight);
                } catch (Exception e) {

                    Log.i("TAG", e.getMessage());
                }*/

                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(user_id).child("Info");

                newPost.put("name", babyName);
                newPost.put("birthday", babyBirthday);
                newPost.put("weight", babyWeight);
                newPost.put("gender", babyGender);

                current_user_db.setValue(newPost);


                Intent r = new Intent(BabyInfoActivity.this,HomeActivity.class);
                startActivity(r);

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(BabyInfoActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    if(mUploadTask != null && mUploadTask.isInProgress()){
                        Toast.makeText(BabyInfoActivity.this, "Upload in progress", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        openGallery();
                    }
                }else{
                    requestStoragePermission();
                }
            }
        });
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed in order to select a photo from your gallery")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BabyInfoActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
            .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(BabyInfoActivity.this, "Upload in progress", Toast.LENGTH_SHORT)
                            .show();
                }
                else{
                    openGallery();
                }
            }else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*// function to make a new baby Category
    private void produceANewBabyIntoDatabase(String babyName, String babyBirthDate,
                                             int babyWeight, String babyGender) {
        if(TextUtils.isEmpty(uniqueBabyID)){
            uniqueBabyID = databaseReference.push().getKey();
        }
        // here we are creating a new baby object
        Baby baby = new Baby(babyName, babyBirthDate , babyWeight, babyGender);
        // here we are creating a category called "Baby"
        databaseReference.child(uniqueBabyID).setValue(baby);
    }*/


    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
//            mImageView.setImageURI(imageUri);
            /*try {
                *//*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mImageView.setImageBitmap(bitmap);*/
                Picasso.get().load(imageUri).into(mImageView);
                
                uploadImageToFirebaseStorage();

/*            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadImageToFirebaseStorage() {

        if(imageUri != null){
            StorageReference babyImg = mStorageRef.child(mAuth.getCurrentUser().getUid())
                    .child("profile_pic").child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));

            mUploadTask = babyImg.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
//                            Upload upload = new Upload(taskSnapshot.getDownloadUrl().toString());
                            /*String uploadId = mDatabaseRef.push().getKey();*/

//                            String user_id = mAuth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = mDatabaseRef.child("Users").child(user_id);

                            newPost.put("profile_pic", taskSnapshot.getDownloadUrl().toString());

//                            current_user_db.setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}