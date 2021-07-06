package com.example.myhealthapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.Reference;

public class MainActivity extends AppCompatActivity {

    private static final int Take_Image_Code= 1000;
    private static int LOCATION_PERMISSION_REQUEST = 1;
    private static final String TAG= "TAG";

    private Button logOut;
    private FirebaseAuth mAuth;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImageView = findViewById(R.id.profile_image_view);
        logOut = findViewById(R.id.BlogOut);
        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            if(user.getPhotoUrl()!=null)
            {
                Glide.with(this).load(user.getPhotoUrl()).into(profileImageView);
            }
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                mAuth.signOut();
                finish();

            }
        });


    }


    public void profileImage(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager())!=null)
  //      {
            startActivityForResult(intent,Take_Image_Code);
            Log.e(TAG,"hello world");
    //    }




    }

    public void cvMapsFunction(View view) {


    mapsPermissionRequest();



       }

    private void mapsPermissionRequest()
    {

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
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
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
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
                    profileImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
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
                Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Profile image load failed !", Toast.LENGTH_SHORT).show();


            }
        });
    }

    }