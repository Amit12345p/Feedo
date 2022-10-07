package com.example.feedo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.util.Map;

public class AccountSetting extends AppCompatActivity {

    //firebase
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fstore;
    String userId;
    DocumentReference documentReference;


    EditText txtUsername,txtEmail,txtPhoneNo,txtPassword;
    ConstraintLayout btnUpdate;

    String _username,_email,_phoneNo;  //global variable to hold data inside this acitiviy


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        txtUsername=findViewById(R.id.txtUsername);
        txtEmail=findViewById(R.id.txtEmail);
        txtPhoneNo=findViewById(R.id.txtPhoneNo);
       // txtPassword=findViewById(R.id.txtPassword);

        btnUpdate=findViewById(R.id.btnUpdate);


        //firebase
        fAuth= FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        user=fAuth.getCurrentUser();
        userId=fAuth.getCurrentUser().getUid();
        documentReference=fstore.collection("users").document(userId);


        updateData();

        //update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewData();
                Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

    }




    private void updateData() {

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                txtEmail.setText(documentSnapshot.getString("email"));
                txtUsername.setText(documentSnapshot.getString("username"));
                txtPhoneNo.setText(documentSnapshot.getString("phone_no"));
             //  txtPassword.setText(documentSnapshot.getString("password"));
            }
        });
    }



    //to save updated data
    private void updateNewData() {
        String username=txtUsername.getText().toString().trim();
        String email=txtEmail.getText().toString().trim();
        String phone=txtPhoneNo.getText().toString().trim();
     //  String password=txtPassword.getText().toString().trim();

        documentReference.update("username",username);
        documentReference.update("email",email);
        documentReference.update("phone_no",phone);
      // documentReference.update("password",password);

//       user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//           @Override
//           public void onComplete(@NonNull Task<Void> task) {
//               if(task.isSuccessful()){
//                   Toast.makeText(AccountSetting.this, "email updated ", Toast.LENGTH_SHORT).show();
//               }
//           }
//       });


    }



}