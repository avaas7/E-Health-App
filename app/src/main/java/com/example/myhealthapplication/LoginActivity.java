package com.example.myhealthapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    private EditText etEmail2;
    private EditText etPassword2;
    private TextView bSignUp2;
    private Button tvLogin2;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmail2 = findViewById(R.id.etEmail2);
        etPassword2 = findViewById(R.id.etPassword2);
        tvLogin2 = findViewById(R.id.ButtonLogin2);
        bSignUp2 = findViewById(R.id.tvSignUp2);
        progressBar2 = findViewById(R.id.progressBar2);

        mAuth= FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!= null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }



        tvLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = etEmail2.getText().toString().trim();
                String pass = etPassword2.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    etEmail2.setError("Please enter your Email");
                    return;
                }


                if(TextUtils.isEmpty(pass))
                {
                    etPassword2.setError("Please enter your Password");
                    return;
                }


                if (pass.length()<6)
                {
                    etPassword2.setError("pass must be of atleast 6 characters");
                    return;
                }


                progressBar2.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this,"Logged in successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(LoginActivity.this,"Error occured"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });

        bSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });



    }

    }
