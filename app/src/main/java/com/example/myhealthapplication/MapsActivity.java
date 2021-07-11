package com.example.myhealthapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int LOCATION_PERMISSION_REQUEST = 1;
    private Location lastKnownLocation;
    private LocationRequest locationRequest;

    FloatingActionButton bFloatingMap;
    GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bFloatingMap = findViewById(R.id.BfloatingMap);


        mLocationClient = new FusedLocationProviderClient(this);


        initMap();
//        locationPermissionRequest();
    }

    private void initMap() {
        SupportMapFragment supportMapFragment4 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragMap);
        supportMapFragment4.getMapAsync(this);
    }

    private boolean providerEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return providerEnable;
    }


    public void bFloatingMap(View view) {
        getCurrentLocation();
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

        Log.e(TAG, "GET CURRENT LOCATION");

        if (!providerEnable()) {
            return;
        }

        Task<Location> taskLocation = mLocationClient.getLastLocation();
        taskLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.e(TAG, "v" + "task1");
                if (task.isSuccessful()) {
                    lastKnownLocation = task.getResult();
                    Log.e(TAG, "l " + String.valueOf(task.getResult()));
                    try {
       //                 goToLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                      if (lastKnownLocation != null) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), 15));
                       }

                    } catch (Exception e) {
                        Log.e(TAG, "a" + e.getMessage());
                    }

                } else {
                    Log.e(TAG, "b" + task.getException().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "c" + e.toString());
            }
        });
    }

    private void goToLocation(double latitude, double longitude) {
        Log.e(TAG, "task3");
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
     //   MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your current location");
        mGoogleMap.moveCamera(cameraUpdate);
        /*mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
*/
    }
    private void mapsPermissionRequest()
    {

        if(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"already permission granted");
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {

                new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed for accessing your location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        updateLocationUI();
        getCurrentLocation();
        Log.e(TAG, "ON MAP READY");
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }
        try {
            if (providerEnable()) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                mapsPermissionRequest();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"permission granted");
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"permission denied");
            }
        }
    }
}