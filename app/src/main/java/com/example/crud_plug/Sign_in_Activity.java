package com.example.crud_plug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_in_Activity extends AppCompatActivity {

    EditText email,password;
    Button btn_signIn;
    TextView signUp;
    TextView signUpText;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStatelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        btn_signIn = findViewById(R.id.signIn_button);
        signUp = findViewById(R.id.textView_newAccount);



        mAuthStatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(Sign_in_Activity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Sign_in_Activity.this,MainActivity.class));
                }
            }
        };

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (userEmail.isEmpty()){
                    Toast.makeText(Sign_in_Activity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("Please enter your email");
                    email.requestFocus();

                } else if (userPassword.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                    Toast.makeText(Sign_in_Activity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else if (userEmail.isEmpty() && userPassword.isEmpty()){
                    Toast.makeText(Sign_in_Activity.this, "Both fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!userEmail.isEmpty() && !userPassword.isEmpty()){
                    mFirebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(Sign_in_Activity.this,MainActivity.class));
                            } else{
                                Toast.makeText(Sign_in_Activity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Sign_in_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_in_Activity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStatelistener);
    }
}
