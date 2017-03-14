package com.ursa_studio.booster.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ursa_studio.booster.R;
import com.ursa_studio.booster.model.Boost;
import com.ursa_studio.booster.order.OrderActivity;

public class MapsMainActivity extends AppCompatActivity
    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener,
    GoogleMap.OnMarkerClickListener {

  private static final int REQUEST_FINE_LOCATION = 1000;
  public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
  public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
      UPDATE_INTERVAL_IN_MILLISECONDS / 2;
  protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
  protected final static String LOCATION_KEY = "location-key";

  private static final String TAG = "LOC";
  private GoogleMap mMap;

  private GoogleApiClient mGoogleApiClient;
  private Location mLastLocation;
  protected LocationRequest mLocationRequest;
  protected Boolean mRequestingLocationUpdates;
  private Boost boost;


  @Override protected void onStart (){
    super.onStart();
    mGoogleApiClient.connect();
  }

  protected void onStop (){
    mGoogleApiClient.disconnect();
    super.onStop();
  }

  @Override protected void onResume (){
    super.onResume();
    mGoogleApiClient.connect();
  }

  @Override protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    getSupportActionBar().setTitle(getString(R.string.app_name));

    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    mRequestingLocationUpdates = false;
    updateValuesFromBundle(savedInstanceState);

    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions(MapsMainActivity.this,
          new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_FINE_LOCATION);
    }

    if(mGoogleApiClient == null){
      mGoogleApiClient = new GoogleApiClient.Builder(this).
          addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
      createLocationRequest();
    }
    createLocationRequest();

    boost = new Boost();

    findViewById(R.id.btnNext).setOnClickListener(this);
  }

  @Override protected void onRestart (){
    super.onRestart();
    mMap.clear();
  }

  @Override public boolean onCreateOptionsMenu (Menu menu){
    getMenuInflater().inflate(R.menu.main_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected (MenuItem item){
    switch(item.getItemId()){

      default:

        return super.onOptionsItemSelected(item);
    }
  }

  protected void createLocationRequest (){
    mLocationRequest = new LocationRequest();

    mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

    mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
  }

  @Override public void onLocationChanged (Location location){
    mLastLocation = location;
  }

  @Override public void onConnected (Bundle connectionHint){

    try{
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
      if(mLastLocation != null){

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
            new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
            Constants.MAP_ZOOM));
      } else{

        showDialog();
      }
    } catch(SecurityException e){
      ActivityCompat.requestPermissions(MapsMainActivity.this,
          new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_FINE_LOCATION);
    }
    startLocationUpdates();
  }

  protected void startLocationUpdates () throws SecurityException{

    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
        this);
  }

  private void updateValuesFromBundle (Bundle savedInstanceState){
    Log.i(TAG, "Updating values from bundle");
    if(savedInstanceState != null){

      if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)){
        mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
      }

      if(savedInstanceState.keySet().contains(LOCATION_KEY)){

        mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
      }
    }
  }

  private void showDialog (){

    new AlertDialog.Builder(MapsMainActivity.this).setTitle(
        getString(R.string.enable_location_access))
        .setMessage(getString(R.string.enable_location_access_text))
        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
          public void onClick (DialogInterface dialog, int which){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
          }
        })
        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
          public void onClick (DialogInterface dialog, int which){
            dialog.cancel();
          }
        })
        .setIcon(android.R.drawable.ic_dialog_info)
        .show();
  }

  @Override public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults){
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch(requestCode){

      case (REQUEST_FINE_LOCATION):
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
          mGoogleApiClient.connect();
        }
        break;
    }
  }

  @Override public void onMapReady (GoogleMap googleMap) throws SecurityException{
    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    googleMap.getUiSettings().setCompassEnabled(true);
    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    mMap = googleMap;
    mMap.setMyLocationEnabled(true);
    googleMap.setOnMarkerClickListener(this);
    if(mLastLocation != null){
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
          new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
          Constants.MAP_ZOOM));
    }
    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override public void onMapClick (LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        boost.setLat(latLng.latitude);
        boost.setLon(latLng.longitude);

      }
    });
  }

  @Override public void onConnectionSuspended (int i){

    mGoogleApiClient.connect();
  }

  @Override public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

  }

  @Override public boolean onMarkerClick (Marker marker){

    return true;
  }
  @Override public void onClick (View view){

    switch (view.getId ()) {
      case R.id.btnNext:
        if (validatecarLocation()){

          Intent iExp=new Intent(MapsMainActivity.this,OrderActivity.class);
          iExp.putExtra(Constants.BOOST_OBJECT,boost);
          startActivity(iExp);


        }
        break;

    }

  }

  private boolean validatecarLocation(){

    if ((boost.getLat() == null) ){

      Toast.makeText(MapsMainActivity.this,getString(R.string.car_pin_not_set),Toast.LENGTH_SHORT).show();
      return false;

    }
    else {
      return true;
    }

  }
}
