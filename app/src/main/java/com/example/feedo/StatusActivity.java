package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class StatusActivity extends AppCompatActivity {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference mDonationDataReference = mDb.collection("donationData");
    public static final String TAG = "TAG";
    TextView mTxtDonarName, mTxtPhoneNo, mTxtAddress, mTxtDonationType, mTxtCookedBefore, mTxtPlaceType, mTxtStatus;
    String mGetAcceptedValue, mGetType;
    Button mBtnDeliverd;
    CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // TO HIDE ACTION BAR AND SET FULLSCREEN COMPATIBLE
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mTxtDonarName = findViewById(R.id.txtDonarName);
        mTxtPhoneNo = findViewById(R.id.txtPhoneNo);
        mTxtAddress = findViewById(R.id.txtAddress);
        mTxtDonationType = findViewById(R.id.txtDonationType);
        mTxtCookedBefore = findViewById(R.id.txtCookedBefore);
        mTxtPlaceType = findViewById(R.id.txtPlaceType);
        mTxtStatus = findViewById(R.id.txtStatus);
        mBtnDeliverd =findViewById(R.id.btnDeliverd);
        mCardView =findViewById(R.id.cardView);
        // FOR TEMPORARY USE
        mGetAcceptedValue = getIntent().getStringExtra("accepted");

        //FUNCTION TO LOAD DATA FROM FIREBASE
        loadData(mGetAcceptedValue, mGetType);

        // ACTION ON BUTTON DELIVERED
        mBtnDeliverd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtStatus.setText("Delivered");
                mBtnDeliverd.setVisibility(view.INVISIBLE);
                mCardView.setCardBackgroundColor(getResources().getColor(R.color.delivered, null));
            }
        });

    }

    // FUNCTION TO LOAD DATA FROM FIREBASE
    private void loadData(String getAcceptedValue,String getType) {
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

                      //  String actionType=(String) document.get("actionType");
                      //  txtType.setText(getType);

                        mTxtDonarName.setText(donarName);
                        mTxtPhoneNo.setText(phoneNo);
                        mTxtAddress.setText(address);
                        mTxtDonationType.setText(donationType);
                        mTxtCookedBefore.setText(cookedBefore);
                        mTxtPlaceType.setText(chooseTypeOfPlace);
                        mTxtStatus.setText("Approved");
                        mCardView.setCardBackgroundColor(getResources().getColor(R.color.approved, null));
                    }

                } else {
                    Log.d(TAG, "Error fetching data: ", task.getException());
                }


            }

        });

    }
}









