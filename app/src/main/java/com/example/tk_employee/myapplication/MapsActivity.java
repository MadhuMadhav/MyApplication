package com.example.tk_employee.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnCameraIdleListener,LocationListener{

    Button nxtButton;
    TextView tvLocation;
    ImageView imgMarker;
    private GoogleMap mMap;
    RelativeLayout relativeLayout;
    Geocoder geocoder;
    Location location;
    LocationManager locationManager;
    List<Address> addresses = new ArrayList<Address>();

    Boolean isConnectionExist = false;
    MobileInternetConnectionDetector cd;

    static String lat, lang;
    View mapView;
    double latitude, longitude, cLat, cLong;

    public static final String SHARED_PREF = "sharedPrefs";

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    SupportMapFragment mapFragment;
    private boolean isFirstMapCall = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getPermissions();
        imgMarker = findViewById(R.id.marker_pointer);
        relativeLayout = findViewById(R.id.root);
        tvLocation = findViewById(R.id.tv_search);

        cd = new MobileInternetConnectionDetector(getApplicationContext());
        isConnectionExist = cd.checkMobileInternetConn();

        if (isConnectionExist) {
            getLocation();

            nxtButton = findViewById(R.id.nxtButton);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           // mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();

            nxtButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this, PlacesActivity.class);
                    startActivity(intent);
                }
            });

            tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    try {
                        intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapsActivity.this);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                    //Intent intent = new Intent(MapsActivity.this, SearchActivity.class);
                    //startActivity(intent);
                }
            });
        } else {
            Toast.makeText(MapsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //LatLng Hyderabad = new LatLng(lat, lang);
        //mMap.addMarker(new MarkerOptions().position(Hyderabad).title("Banjara Hills"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Hyderabad));

        //mMap.setMyLocationEnabled(true);

        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        //location = locationManager.getLastKnownLocation(provider);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnCameraIdleListener(this);

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 150);
        //setCurrentLocation();

      //  getLocation();
        setCurrentLocation();

        /*if (location != null) {

            SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.e("location","Latitude:"+latitude+"  Longitude:"+longitude);

            editor.putString("latitude", String.valueOf(latitude));
            editor.putString("longitude", String.valueOf(longitude));
            editor.apply();

            setCurrentLocation();
        }
        else
        {
            Log.e("location","Location Is Null");
        }*/
    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            // mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                tvLocation.setText(place.getAddress());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                Log.i("place", "Place: " + place.getName() + " LatLang: " + place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("place", "Error: " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                Log.i("place", "User cancelled");
            }
        }
    }

    private void setCurrentLocation() {

        LatLng coordinate = new LatLng(latitude,longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
        mMap.animateCamera(yourLocation);
    }

    void getLocation() {
        try {
           /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                 mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }*/
            Log.i("getln","inside getLocation");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1*1000,
                    1, this); //NETWORK_PROVIDER
        }
        catch(SecurityException e) {
            Log.i("getln","getLocation...inside catch Block");
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("getln","inside onLocationChanged Method");

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("latitude", String.valueOf(latitude));
        editor.putString("longitude", String.valueOf(longitude));
        editor.commit();

        if(isFirstMapCall)
        {
            isFirstMapCall=false;
            mapFragment.getMapAsync(this);
        }

       /* LatLng coordinate = new LatLng(latitude,longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);

        mMap.animateCamera(yourLocation);*/
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLocation();
        Log.i("centerLatLong", "latitude:" + latitude+" longitude:" + longitude);
        setCurrentLocation();
        return true;
    }

    @Override
    public void onCameraIdle() {
        Log.i("getln","inside onCameraIdle Method");
        LatLng midLatLng = mMap.getCameraPosition().target;
        lat = String.valueOf(midLatLng.latitude);
        lang = String.valueOf(midLatLng.longitude);
        String loc=getLocationDetails(lat,lang);
        tvLocation.setText(loc);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("LocationChange","onStatusChanged "+provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("LocationChange","onProviderEnabled "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("LocationChange","onProviderDisabled "+provider);
    }

    public String getLocationDetails(String lat,String lang)
    {
        String address = "Location Not Found";
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            cLat= Double.parseDouble(lat);
            cLong= Double.parseDouble(lang);
            addresses = geocoder.getFromLocation(cLat, cLong, 1);
            Log.i("address","Got Current Address");
            address = addresses.get(0).getAddressLine(0);
            Log.i("city","result "+address);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("address","Error In Fetching Current Address");
        }
        return address;
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        // String city = addresses.get(0).getLocality();
       /* String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();*/
    }
}
