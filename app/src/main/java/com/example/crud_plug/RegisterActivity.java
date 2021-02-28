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

public class RegisterActivity extends AppCompatActivity {

    EditText email,password,confirmPassword;
    Button btn_signUp;

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        confirmPassword = findViewById(R.id.editText_confirm_password);
        btn_signUp = findViewById(R.id.signUp_button);

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if (userEmail.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("Please enter your email");
                    email.requestFocus();

                } else if (userPassword.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else if (userEmail.isEmpty() && userPassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Both fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if (!userEmail.isEmpty() && !userPassword.isEmpty()){

                    if (confirm.equals(userPassword))
                    {
                        mFirebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        {
                        Toast.makeText(RegisterActivity.this, "Two passwords don't match! try again", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                        confirmPassword.setText("");
                    }

                }

            }

        });
    }
}
