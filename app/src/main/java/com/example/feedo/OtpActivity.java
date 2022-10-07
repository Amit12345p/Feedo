package com.example.feedo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class OtpActivity extends AppCompatActivity {

    TextView getPhoneNo;
    EditText otp1,otp2,otp3,otp4,otp5,otp6;


    String getbackendotp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        getPhoneNo=findViewById(R.id.getPhoneNo);
        Intent intent=getIntent();
        String phoneNo=intent.getStringExtra("phoneNo");
        getPhoneNo.setText("+91-"+phoneNo);

        getbackendotp=getIntent().getStringExtra("backendotp");


        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp5=findViewById(R.id.otp5);
        otp6=findViewById(R.id.otp6);
        final Button validateOtp=findViewById(R.id.validateOtp);

        //progress bar
        final ProgressBar progressBar=findViewById(R.id.progressBarOtpVerify);




        validateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!otp1.getText().toString().trim().isEmpty() && !otp2.getText().toString().trim().isEmpty() && !otp3.getText().toString().trim().isEmpty() &&
                        !otp4.getText().toString().trim().isEmpty() && !otp5.getText().toString().trim().isEmpty() && !otp6.getText().toString().trim().isEmpty()){

                    String enterCodeOtp=otp1.getText().toString()+
                            otp2.getText().toString()+
                            otp3.getText().toString()+
                            otp4.getText().toString()+
                            otp5.getText().toString()+
                            otp6.getText().toString();

                    if(getbackendotp!=null){
                        progressBar.setVisibility(View.VISIBLE);
                        validateOtp.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                                getbackendotp,enterCodeOtp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        validateOtp.setVisibility(View.VISIBLE);
                                        if(task.isSuccessful()){
                                            Toast.makeText(OtpActivity.this, "Registered Successfully.Please check your email for verification.", Toast.LENGTH_SHORT) .show();
                                            Intent intent=new Intent(OtpActivity.this,LoginActivity.class);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();


                                        }else{
                                            Toast.makeText(OtpActivity.this, "Please enter correct OTP", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                             }else{
                                   Toast.makeText(OtpActivity.this, "Please check your internet Connection!", Toast.LENGTH_SHORT).show();
                               }
                                     //Toast.makeText(OtpActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                         }else{
                                Toast.makeText(OtpActivity.this, "Please enter OTP ", Toast.LENGTH_SHORT).show();
                          }
                //progress bar
//                progressBar.setVisibility(view.VISIBLE);
//                validateOtp.setVisibility(view.INVISIBLE);
            }
        });

          numberotpmove();

        //resend otp
       TextView resentOtp = findViewById(R.id.resentOtp);

        //resend otp method
        resentOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otp sending to respective phone number
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNo,
                        60,
                        TimeUnit.SECONDS,
                        OtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks()     {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getbackendotp=newbackendotp;
                                Toast.makeText(OtpActivity.this, "OTP has been sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });



    }

    private void numberotpmove() {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(!s.toString().trim().isEmpty()){
                   otp2.requestFocus();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    otp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}