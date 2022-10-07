package com.example.feedo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mFauth;
    FirebaseFirestore mFstore;
    String mUserId;
    ConstraintLayout mBtnAccountSetting, mBtnPersonalInfo;
    TextView mTxtEmail, mTxtUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mBtnAccountSetting =findViewById(R.id.btnAccountSetting);
        mBtnPersonalInfo =findViewById(R.id.btnPersonalInfo);
        mTxtEmail =findViewById(R.id.txtEmail);
        mTxtUsername =findViewById(R.id.txtUsername);
        mFauth = FirebaseAuth.getInstance();
        mFstore =FirebaseFirestore.getInstance();
        mUserId = mFauth.getCurrentUser().getUid();

        //FUNCTION CALL TO LOAD USER INFORMATION FROM FIREBASE
        loadProfile();

        //INTENT PASSING FROM PROFILE ACTIVITY TO ACCOUNT SETTING
        mBtnAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AccountSetting.class);
                startActivity(i);
            }
        });


        //INTENT PASSING FROM PROFILE ACTIVITY TO PERSONALINFO ACTIVITY
        mBtnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),PersonalInfoActivity.class);
                startActivity(i);
            }
        });


    }

    //FUNCTION TO LOAD DATA FROM FIREBASE
    private void loadProfile() {
        DocumentReference mDocumentReference= mFstore.collection("users").document(mUserId);
        mDocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                 mTxtEmail.setText(documentSnapshot.getString("email"));
                 mTxtUsername.setText(documentSnapshot.getString("username"));
            }
        });

    }

}