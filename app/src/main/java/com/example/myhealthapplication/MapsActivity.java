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

    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 3;

    FloatingActionButton bFloatingMap;
    GoogleMap mGoogleMap;
    private static int GPS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mLocationClient;
    private static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bFloatingMap = findViewById(R.id.BfloatingMap);
        initMap();
        locationPermissionRequest();

        mLocationClient = new FusedLocationProviderClient(this);
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
       // locationPermissionRequest();
        getCurrentLocation();
    }

    private void locationPermissionRequest() {
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
                    Toast.makeText(MapsActivity.this, "Gps is on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Gps is off", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Log.e(TAG, "GET CURRENT LOCATION");
        mLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.e(TAG, "task1");
                if (task.isSuccessful()) {
                    Log.e(TAG, "task2");
                    Location location = task.getResult();
                    Log.e(TAG, "taskX");
                    Log.e(TAG, String.valueOf(task.getResult()));
                    goToLocation(location.getLatitude(), location.getLongitude());
                    Log.e(TAG, "taskY");

                } else {
                    Log.e(TAG, task.getException().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    private void goToLocation(double latitude, double longitude) {
        Log.e(TAG, "task3");
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your current location");
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
            Log.e(TAG,"ON MAP READY");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {

            if (providerEnable()) {
                Toast.makeText(this, "Location enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location denied", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == REQUEST_CHECK_SETTINGS)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_SHORT).show();
                    getCurrentLocation();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS is denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Log.e(TAG, "onstart");
        if (!providerEnable()) {
            Log.e(TAG, "onstart");
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Location permission").setMessage("Please enable permission for getting location services")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }).setCancelable(false).show();

        }*/
    }

}
