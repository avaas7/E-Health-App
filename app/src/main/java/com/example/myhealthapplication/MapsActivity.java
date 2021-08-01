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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.myhealthapplication.R.drawable.health_icon_1;
import static com.example.myhealthapplication.R.drawable.ic_hospital;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int LOCATION_PERMISSION_REQUEST = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE = 2;
    private Location lastKnownLocation;
    private LocationRequest locationRequest;

    ImageButton directionButton,callButton;
    TextView tvPlaceName;
    LinearLayout callDirectionLayout;

    FloatingActionButton bFloatingMap;
    GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    Double lastLocationLat = 0.0;
    Double lastLocationLng =0.0;
    LatLng lastLocationLatLng;
    private static String TAG = "TAG";

    private String hosName;
    private String hosTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bFloatingMap = findViewById(R.id.BfloatingMap);

        directionButton = findViewById(R.id.directionButton);
        callButton = findViewById(R.id.call_button);

        tvPlaceName = findViewById(R.id.place_name_text_view);
        callDirectionLayout = findViewById(R.id.callDirectionLayout);

        mLocationClient = new FusedLocationProviderClient(this);


        initMap();
//        locationPermissionRequest();

/*
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyBOWYO2giuSNi55EHapqCpw9kO-5hISkd4");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autoCompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


         autoCompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME));

        autoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.e(TAG,"Places :" + place.getName()+ ", " + place.getId() );
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG,"Error : "+ status.toString());
            }
        });




/*
        autoCompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(27.777249, 85.391118),
                new LatLng(27.649595, 85.227696)));

        autoCompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(27.777249, 85.391118),
                new LatLng(27.649595, 85.227696)));

        autoCompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        autoCompleteFragment.setCountries("NP");
*/


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
/*
        if (!providerEnable()) {
            return;
        }
*/
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
                        lastLocationLat=  lastKnownLocation.getLatitude();
                        lastLocationLng = lastKnownLocation.getLongitude();
                        lastLocationLatLng = new LatLng(lastLocationLat,lastLocationLng);
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    lastLocationLatLng, 15));


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

        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                hosName = marker.getTitle();
                hosTel = marker.getSnippet();
                Log.e(TAG, hosName+ ","+ hosTel);
                callDirectionLayout.setVisibility(View.VISIBLE);
                tvPlaceName.setText(hosName);

                //displayTrack(String.valueOf(lastLocationLat)+","+String.valueOf(lastLocationLng),marker.getTitle());




                return false;
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void hospitalButton(View view) {
        double lat1=27.704877126306243;
        double lng1 =85.31364066858647;
        LatLng latLng1 = new LatLng(lat1, lng1);
        LatLng latLngKtm = new LatLng(27.7186687209129,85.32475706287931);

        ArrayList<MarkerData> markerDataArrayList = new ArrayList<MarkerData>();

        MarkerData birHospital = new MarkerData(new LatLng(27.704877126306243,85.31364066858647),"Bir hospital", "014223071");
        MarkerData medicitiHospital = new MarkerData(new LatLng(27.663531415218365, 85.3025148354194),"Nepal Mediciti hospital","014217766");
        MarkerData norvic = new MarkerData(new LatLng(27.69294565746874, 85.31957684356331),"Norvic hospital","015970032");
        MarkerData tuTeaching = new MarkerData(new LatLng(27.736519660091947, 85.32998451103599),"TU teaching hospital","014412303");
        MarkerData patanHospital = new MarkerData(new LatLng(27.672272012105456, 85.32077847318266),"Patan Hospital","015522295");
        MarkerData gangalalHospital = new MarkerData(new LatLng(27.746396969765783, 85.34221654641101),"Gangalal Hospital", "014371322");
        MarkerData birendraHospital = new MarkerData(new LatLng( 27.71169421102254, 85.29136561444946)," Birendra Military Hospital", "014271941");

       markerDataArrayList.add(birHospital);
        markerDataArrayList.add(medicitiHospital);
        markerDataArrayList.add(norvic);
        markerDataArrayList.add(tuTeaching);
        markerDataArrayList.add(patanHospital);
        markerDataArrayList.add(gangalalHospital);
        markerDataArrayList.add(birendraHospital);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngKtm, 12);
        mGoogleMap.moveCamera(cameraUpdate);

        for (int i=0 ; i<markerDataArrayList.size();i++)
        {
            createMarker(markerDataArrayList.get(i).getMarkerLanLng(),markerDataArrayList.get(i).getMarkerTitle(),markerDataArrayList.get(i).getTel());
        }

        /*     MarkerOptions markerOptions = new MarkerOptions().position(latLng1).title("Bir Hospital + Contact: +97714221119").icon(BitmapFromVector(getApplicationContext(), ic_hospital));
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.addMarker(markerOptions);
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
   */ }


    private void createMarker(LatLng markerLatLng, String markerTitle,String tel)
    {
           MarkerOptions markerOptions = new MarkerOptions().position(markerLatLng).snippet(tel).title(markerTitle).icon(BitmapFromVector(getApplicationContext(),ic_hospital));
        mGoogleMap.addMarker(markerOptions);

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a d/rawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void displayTrack(String sSource,String sDestination) {
        try {

            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setPackage("com.google.android.apps.maps");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.maps");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    public void showDirection(View view) {
        if(hosName!=null)
        displayTrack(String.valueOf(lastLocationLat)+","+String.valueOf(lastLocationLng),hosName);

    }

    public void callHospital(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+hosTel));
        startActivity(intent);
    }
}