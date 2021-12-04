package com.example.myhealthapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myhealthapplication.Maps.MapsActivity;
import com.example.myhealthapplication.Newsfeed.MainActivity;
import com.example.myhealthapplication.Quiz.StartingScreenActivity;
import com.example.myhealthapplication.SignIn.LoginActivity;
import com.example.myhealthapplication.notes.NoteMainActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity1 extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 3;
    private LocationRequest locationRequest;

    private static final int Take_Image_Code= 1000;
    private static int LOCATION_PERMISSION_REQUEST = 1;
    private static final String TAG= "TAG";



    private Button logOut;
    private FirebaseAuth mAuth;
    private ImageView profileImageView1;

    private TextView changeTextView;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        mNavigationView = findViewById(R.id.nav_view);

        View header = mNavigationView.getHeaderView(0);
        profileImageView1 = (ImageView) header.findViewById(R.id.profile_image_view1);
        changeTextView = (TextView) header.findViewById(R.id.tv_profile_homepage);

        logOut = findViewById(R.id.BlogOut);
        mAuth = FirebaseAuth.getInstance();



        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            if(user.getPhotoUrl()!=null)
            {
                Glide.with(this).load(user.getPhotoUrl()).into(profileImageView1);
            }
        }



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });



        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager())!=null)
                //      {
                startActivityForResult(intent,Take_Image_Code);
                Log.e(TAG,"hello world");
                //    }
            }
        });
    }

/*
    public void profileImage(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager())!=null)
  //      {
            startActivityForResult(intent,Take_Image_Code);
            Log.e(TAG,"hello world");
    //    }




    }
*/
    public void cvMapsFunction(View view) {




        locationpermissionRequest();
//        mapsPermissionRequest();



       }

    private void locationpermissionRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    mapsPermissionRequest();

               //     Toast.makeText(MainActivity.this, "Gps is on", Toast.LENGTH_SHORT).show();


                } catch (ApiException e) {
                    e.printStackTrace();

                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity1.this,REQUEST_CHECK_SETTINGS);  //dialog ask for location
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });


    }

    private void mapsPermissionRequest()
    {

        if(ContextCompat.checkSelfPermission(MainActivity1.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"already permission granted");
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {

                new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed for accessing your location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity1.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
                                Log.e(TAG,"permission requested dialog");
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
                Log.e(TAG,"permission requested");
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"permission granted");
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"permission denied");
               /* Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("Package",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);*/

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Take_Image_Code)
        {
            switch (resultCode)
            {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageView1.setImageBitmap(bitmap);

                    handleUpload(bitmap);
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
        if(requestCode == REQUEST_CHECK_SETTINGS)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:
                //    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_SHORT).show();
                    mapsPermissionRequest();

                    break;
                case Activity.RESULT_CANCELED:
               //     Toast.makeText(this, "GPS is denied", Toast.LENGTH_SHORT).show();
            }

        }



    }

    public void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);

        String uId = mAuth.getCurrentUser().getUid();

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Profile Images").child(uId + ".jpeg");
//putbytes means upload to specific storage reference, to Byte array is obtaining the byte array
        reference.putBytes(boas.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(reference);
            }
        }).
                addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onfailure",e.getCause());
            }
        });

    }

    public void getDownloadUrl(StorageReference reference)
    {
           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   Log.e(TAG,"onSuccess:" + uri);
                   setUserUri(uri);

               }
           });
    }

    public  void setUserUri( Uri uri)
    {
        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();

        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG,"hello world 3");
                Toast.makeText(MainActivity1.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity1.this, "Profile image load failed !", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void startNoteMainActivity(View view) {
        startActivity(new Intent(this, NoteMainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startQuizActivity(View view) {
        startActivity(new Intent(this, StartingScreenActivity.class));
    }

    public void cvNewsfeed(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}