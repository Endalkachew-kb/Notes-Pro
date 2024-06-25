package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
 EditText email,password,confirm;
 Button create;
 ProgressBar pb;
 TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        confirm=findViewById(R.id.CPasswordEditText);
        create=findViewById(R.id.create_account_btn);
        pb=findViewById(R.id.progress_bar);
        login=findViewById(R.id.login_text_view_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                String Confirm=confirm.getText().toString();
                boolean isValidated=validateData(Email,Password,Confirm);
                if(!isValidated){
                    return;
                }
                CreateAccountFirebase(Email,Password);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class));
                finish();

            }
        });
    }
    boolean validateData(String Email,String Password,String Confirm){
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Email is invalid");
            return false;
        }
        if(Password.length()<8){
            password.setError("password length is less than 8 ");
            return  false;
        }
        if(!Password.equals(Confirm)){
            confirm.setError("Password doesn't match");
            return false;
        }
        return true;
    }
    void CreateAccountFirebase(String Email,String Password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(CreateAccountActivity.this,"Successfully  create account,check email for Verify",Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            pb.setVisibility(View.VISIBLE);
            create.setVisibility(View.GONE);
        }else{
            pb.setVisibility(View.GONE);
            create.setVisibility(View.VISIBLE);
        }
    }
}