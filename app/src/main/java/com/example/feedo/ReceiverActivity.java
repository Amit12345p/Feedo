package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReceiverActivity extends AppCompatActivity {

    private FirebaseFirestore Mdb = FirebaseFirestore.getInstance();
    private CollectionReference mDonationDataReference = Mdb.collection("donationData");
    public static final String TAG = "TAG";

    TextView mTxtDonarName, mTxtPhoneNo, mTxtAddress, mTxtDonationType, mTxtCookedBefore, mTxtPlaceType, mTxtStatus;
    Button mBtnAcceptt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        mTxtDonarName = findViewById(R.id.txtDonarName);
        mTxtPhoneNo = findViewById(R.id.txtPhoneNo);
        mTxtAddress = findViewById(R.id.txtAddress);
        mTxtDonationType = findViewById(R.id.txtDonationType);
        mTxtCookedBefore = findViewById(R.id.txtCookedBefore);
        mTxtPlaceType = findViewById(R.id.txtPlaceType);
        mTxtStatus = findViewById(R.id.txtStatus);
        mBtnAcceptt = (Button) findViewById(R.id.btnAccept);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // FUNCTION TO LOAD DATA FROM FIRESTOE IN THE ACTIVITY
        loadData();

        //INTENT PASSING FROM RECEIVER ACTIVITY TO STATUS ACITVITY
        mBtnAcceptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),StatusActivity.class);
                 i.putExtra("accepted",1);
                 i.putExtra("type","Donation");
                startActivity(i);
                 finish();
            }
        });
    }

    //FUNCTION TO LOAD DATA FROM FIREBASE
    private void loadData() {
        mDonationDataReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());//

                        String donarName = (String) document.get("Donar Name");
                        String phoneNo = (String) document.get("Phone Number");
                        String address = (String) document.get("Address");
                        String donationType = (String) document.get("Donation type");
                        String cookedBefore = (String) document.get("Cooked Time");
                        String chooseTypeOfPlace = (String) document.get("Place Type");

                        mTxtDonarName.setText(donarName);
                        mTxtPhoneNo.setText(phoneNo);
                        mTxtAddress.setText(address);
                        mTxtDonationType.setText(donationType);
                        mTxtCookedBefore.setText(cookedBefore);
                        mTxtPlaceType.setText(chooseTypeOfPlace);
                    }

                } else {
                    Log.d(TAG, "Error fetching data: ", task.getException());
                }
            }

        });
    }


}