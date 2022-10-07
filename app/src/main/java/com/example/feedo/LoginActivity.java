package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    TextView mTxtRegister;
    TextInputEditText mTxtEmail, mTxtPassword;
    Button mBtnLogin;
    LinearLayout mBtnGoogle;
    private FirebaseAuth mFAuth;
    TextView mTxtForgotPassword;


    // google sign in
    private GoogleSignInOptions mGso;
    private GoogleSignInClient mGsc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TO HIDE ACTION BAR AND SET FULLSCREEN COMPATIBLE
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mBtnGoogle =findViewById(R.id.btnGoogle);
        mTxtRegister =findViewById(R.id.txtRegister);
        mTxtEmail =findViewById(R.id.txtEmail);
        mTxtPassword =findViewById(R.id.txtpassword);
        mBtnLogin =findViewById(R.id.btnLogin);
        mFAuth =FirebaseAuth.getInstance();
        mTxtForgotPassword =findViewById(R.id.txtForgotPassword);
        ProgressBar mProgressBar=findViewById(R.id.progressBarLogin);

        //SIGN IN OPTION TO USER WITH HIS/HER SAVED CREDENTIALS
        mGso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // ONLY SHOW ACCOUNTS PREVIOUSLY USED TO SIGN IN
                .requestEmail()
                .build();
        //
        mGsc = GoogleSignIn.getClient(this, mGso);

        //ACTION TO LOGIN WTIH GOOGLE
        mBtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        // INTENT PASSING FROM LOGIN ACTIVITY TO REGISTRATION ACTIVITY
        mTxtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),RegistrationAcitivity.class);
                startActivity(i);
                finish();
            }
        });

        // TO AUTHENTICATE USER AND PASSING FROM LOGIN ACTIVITY TO DASHBOARD ACTIVITY
        if(mFAuth.getCurrentUser() !=null){
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        // FOR VALIDATION AND SIGN IN WITH EMAIL ID AND PASSWORD
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mTxtEmail.getText().toString().trim();
                String pass= mTxtPassword.getText().toString().trim();

                mProgressBar.setVisibility(view.VISIBLE);
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

                if(pass.length() < 6)
                {
                    mProgressBar.setVisibility(view.GONE);
                    mTxtPassword.setError("Password Must be >=6 Characters");
                    return;
                }

                // SIGN IN USER WTIH HIS/HER CREDENTIAL
                mFAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(mFAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT) .show();
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                mProgressBar.setVisibility(view.GONE);
                                Toast.makeText(LoginActivity.this, "Please verify your email address!" ,Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            mProgressBar.setVisibility(view.GONE);
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //ACTION FOR FORGOT PASSWORD
        mTxtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mTxtEmail.getText().toString().trim();
                mFAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Reset link has been sent to "+mail, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // LOGIN WITH GOOGLE
    private void loginWithGoogle() {
        Intent intent= mGsc.getSignInIntent();
        startActivityForResult(intent,65);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==65){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                Log.d("Tag","FirebaseAuthWithGoogle:"+account.getId());
                FirebaseAuthWithGoogle(account.getIdToken());
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            } catch (ApiException e) {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // VERIFY WITH IDTOKEN AND SIGN IN
    private void FirebaseAuthWithGoogle(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mFAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("tag","signwithcredential:success");
                            FirebaseUser user= mFAuth.getCurrentUser();
                        }else {
                            Log.w("Tag","signinwithcredential:failure",task.getException());
                        }
                    }
                });
        }


}