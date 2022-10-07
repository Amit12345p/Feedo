package com.example.feedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    CardView mBtnDonate, mBtnReceive, mBtnReceiveStatus, mBtnDonationStatus, mBtnProfile, mBtnLogout;
    FirebaseAuth mFauth;
    private GoogleSignInOptions mGso;
    private GoogleSignInClient mGsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("DashBoard");

        mBtnDonate =findViewById(R.id.btnDonate);
        mBtnReceive =findViewById(R.id.btnReceive);
        mBtnReceiveStatus =findViewById(R.id.btnReceiveStatus);
        mBtnDonationStatus =findViewById(R.id.btnDonationStatus);
        mBtnProfile =findViewById(R.id.btnProfile);
        mBtnLogout =findViewById(R.id.btnLogout);
        mFauth =FirebaseAuth.getInstance();

        //SIGN IN OPTION TO USER WITH HIS/HER SAVED CREDENTIALS
        mGso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGsc = GoogleSignIn.getClient(this, mGso);

        //INTENT PASSING FROM DASHBOARD ACTIVITY TO RECEIVER ACTIVITY
        mBtnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ReceiverActivity.class);
                startActivity(i);
            }
        });

        //INTENT PASSING FROM DASHBOARD ACTIVITY TO DONATE FORM ACTIVITY
        mBtnDonate.setOnClickListener(new View.OnClickListener() {
          //  @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
              //  btnDonate.setCardBackgroundColor(R.color.black);
                Intent i=new Intent(getApplicationContext(),DonateFormActivity.class);
                startActivity(i);

            }
        });

        //INTENT PASSING FROM DASHBOARD ACTIVITY TO STATUS ACTIVITY
        mBtnReceiveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StatusActivity.class);
                startActivity(i);
            }
        });

        //INTENT PASSING FROM DASHBOARD ACTIVITY TO DONATION STATUS ACTIVITY
        mBtnDonationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DonationStatusActivity.class);
                startActivity(i);
            }
        });


        //INTENT PASSING FROM DASHBOARD ACTIVITY TO PROFILE ACTIVITY
        mBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        //INTENT PASSING FROM DASHBOARD ACTIVITY TO MAIN ACTIVITY
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGsc.signOut();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
}