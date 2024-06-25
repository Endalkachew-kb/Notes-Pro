package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    ProgressBar pb;
    TextView create;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        login=findViewById(R.id.login_btn);
        pb=findViewById(R.id.progress_bar);
        create=findViewById(R.id.create_account_text_view_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                boolean isValidated=validateData(Email,Password);
                if(!isValidated){
                    return;
                }
                loginInFirebase(Email,Password);

            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
                finish();
            }
        });

    }

    boolean validateData(String Email,String Password){
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Email is invalid");
            return false;
        }
        if(Password.length()<8){
            password.setError("password length is less than 8 ");
            return  false;
        }

        return true;
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            pb.setVisibility(View.VISIBLE);
           login.setVisibility(View.GONE);
        }else{
            pb.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }
    void loginInFirebase(String Email,String Password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this,"Email is not Verified ,Please verify your Email. ",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}