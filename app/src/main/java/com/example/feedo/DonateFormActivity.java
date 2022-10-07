package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class DonateFormActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    TextInputEditText mTxtDonarName, mTxtPhoneNo, mTxtAddress;
    Spinner mTypeOfDonation, mCookedBefore, mTypeOfPlace1;
    Button mBtnSubmit;

    FirebaseAuth mFauth;
    FirebaseFirestore mFstore;
    String mUserId;
    public static final String mTag = "TAG";

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    private int REQUEST_CODE = 11;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_form);

        mTxtDonarName =findViewById(R.id.txtDonarName);
        mTxtPhoneNo =findViewById(R.id.txtPhoneNo);
        mTxtAddress =findViewById(R.id.txtAddress);
        mTypeOfDonation =findViewById(R.id.typeOfDonation);
        mCookedBefore =findViewById(R.id.cookedBefore);
        mTypeOfPlace1 =findViewById(R.id.typeOfPlace);
        mBtnSubmit =findViewById(R.id.btnSubmit);
        mFauth =FirebaseAuth.getInstance();
        mFstore = FirebaseFirestore.getInstance();

        // GET A HANDLE TO THE FRAGMENT AND REGISTER THE CALLBACK
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    // GET A HANDLE TO THE GOOGLEMAP OBJECT AND DISPLAY MARKER
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        // SET A MARKER IN A GOOGLE MAP
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("you are here");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        mMap.addMarker(markerOptions).showInfoWindow();

       // VALIDATE AND STORE THE DATA TO FIREBASE OF DONATION FORM
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String donarName = mTxtDonarName.getText().toString().trim();
                String phoneNo= mTxtPhoneNo.getText().toString().trim();
                String address= mTxtAddress.getText().toString().trim();
                String donationType= mTypeOfDonation.getSelectedItem().toString().trim();
                String cookedTime= mCookedBefore.getSelectedItem().toString().trim();
                String placeType= mTypeOfPlace1.getSelectedItem().toString().trim();

                String type= "Donor";

                if(TextUtils.isEmpty(donarName))
                {
                    mTxtDonarName.setError("Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(phoneNo))
                {
                    mTxtPhoneNo.setError("Required.");
                    return;
                }
                if(TextUtils.isEmpty(address))
                {
                    mTxtAddress.setError("Name is Required.");
                    return;
                }

                if(donationType.equals("Choose Donation Type")){
                    Toast.makeText(DonateFormActivity.this, "Please select donation type!", Toast.LENGTH_SHORT).show();
                }
                if(cookedTime.equals("Cooked Before")){
                    Toast.makeText(DonateFormActivity.this, "Please select time period!", Toast.LENGTH_SHORT).show();
                }
                if(placeType.equals("Choose Type of Place")){
                    Toast.makeText(DonateFormActivity.this, "Please select place type!", Toast.LENGTH_SHORT).show();
                }

                if(phoneNo.length() < 10)
                {
                    mTxtPhoneNo.setError("Phone number must be valid!");
                    return;
                }

                mUserId = mFauth.getCurrentUser().getUid();
                CollectionReference collectionReference = mFstore.collection("donationData");
                Map<String,Object> user = new HashMap<>();
                user.put("Donar Name",donarName);
                user.put("Phone Number",phoneNo);
                user.put("Address",address);
                user.put("Donation type",donationType);
                user.put("Cooked Time",cookedTime);
                user.put("Place Type",placeType);
                user.put("latitude",location.getLatitude());
                user.put("longitude",location.getLongitude());
                user.put("Userid", mUserId);
                user.put("Type",type);
                user.put("actionType","");

                // ADD USER DATA TO THE COLLECTION REFERENCE IN FIRESTORE
                collectionReference.add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                                Log.d(mTag,"Success!");
                                Intent intent = new Intent(DonateFormActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                                Log.w(mTag, "Error!", e);
                            }
                        });
              }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new com.google.android.gms.location.LocationRequest();
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

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
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    }