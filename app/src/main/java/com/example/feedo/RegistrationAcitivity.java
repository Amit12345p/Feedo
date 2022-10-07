package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegistrationAcitivity extends AppCompatActivity {

    private TextView mTxtLogin;
    public static final String sTag = "TAG";
    private TextInputEditText mTxtUsername, mTxtEmail, mTxtPhoneNo, mTxtPassword;
    private Button mBtnRegister;
    private FirebaseAuth mFAuth;
    private FirebaseFirestore mFStore;
    String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitivity);


        // TO HIDE ACTION BAR AND SET FULLSCREEN COMPATIBLE
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mTxtLogin =findViewById(R.id.txtLogin);
        mFAuth = FirebaseAuth.getInstance();
        mFStore =FirebaseFirestore.getInstance();
        mTxtUsername =findViewById(R.id.txtUsername);
        mTxtEmail =findViewById(R.id.txtEmail);
        mTxtPhoneNo =findViewById(R.id.txtPhoneNo);
        mTxtPassword =findViewById(R.id.txtpassword);
        mBtnRegister =findViewById(R.id.btnRegister);
        ProgressBar mProgressBar=findViewById(R.id.progressBarRegisteration);

        //INTENT PASSING FROM REGISTRATION TO LOGIN ACTIVITY
        mTxtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

       // TO AUTHENTICATE USER AND PASSING FROM REGISTRATION TO DASHBOARD ACTIVITY
        if(mFAuth.getCurrentUser() !=null){
            Intent intent = new Intent(RegistrationAcitivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        //ACTION FOR VALIDATION AND REGISTRATION IN FIREBASE
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mTxtEmail.getText().toString().trim();
                String pass= mTxtPassword.getText().toString().trim();
                String name= mTxtUsername.getText().toString().trim();
                String ph= mTxtPhoneNo.getText().toString().trim();

                if(TextUtils.isEmpty(mail))
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtPassword.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(name))
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtUsername.setError("Username is Required.");
                    return;
                }
                if(TextUtils.isEmpty(ph))
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtPhoneNo.setError("phone number is Required.");
                    return;
                }

                if(ph.length() !=10)
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtPhoneNo.setError("Please enter a valid phone number.");
                    return;
                }



                if(pass.length() < 6)
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtPassword.setError("Password Must be >=6 Characters");
                    return;
                }



                // CREATE USER WITH EMAIL AND PASSWORD BY STORING THE CREDENTIALS IN FIREBASE
                mFAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                             mProgressBar.setVisibility(view.VISIBLE);
                             mBtnRegister.setVisibility(view.INVISIBLE);

                            mUserID = mFAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mFStore.collection("users").document(mUserID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("username",name);
                            user.put("email",mail);
                            user.put("password",pass);
                            user.put("phone_no",ph);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(sTag,"onSuccess: user Profile is created for "+ mUserID);
                                }
                            });

                            //SEND EMAIL VERIFICATION TO THE USER REGISTERED EMAIL
                            mFAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful()){
                                                Toast.makeText(RegistrationAcitivity.this, "Email verification sent successfully!", Toast.LENGTH_SHORT).show();
                                          }else{
                                              Toast.makeText(RegistrationAcitivity.this, "Something went worng!", Toast.LENGTH_SHORT).show();
                                          }
                                        }
                                    });


                            //PHONE NUMBER VERIFICATION BY OTP
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + ph,
                                    60,
                                    TimeUnit.SECONDS,
                                    RegistrationAcitivity.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            mProgressBar.setVisibility(view.GONE);
                                            mBtnRegister.setVisibility(view.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            mProgressBar.setVisibility(view.GONE);
                                            mBtnRegister.setVisibility(view.VISIBLE);
                                            Toast.makeText(RegistrationAcitivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            mProgressBar.setVisibility(view.GONE);
                                            mBtnRegister.setVisibility(view.VISIBLE);

                                            Intent intent = new Intent(RegistrationAcitivity.this, OtpActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("phoneNo",ph);
                                            intent.putExtra("backendotp",backendotp);
                                            startActivity(intent);
                                        }
                                    }
                            );


                        }
                        else{
                            mProgressBar.setVisibility(view.GONE);
                            mBtnRegister.setVisibility(view.VISIBLE);
                            Toast.makeText(RegistrationAcitivity.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });














    }


}