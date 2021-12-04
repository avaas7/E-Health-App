package com.example.myhealthapplication.SignIn;

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

import com.example.myhealthapplication.MainActivity1;
import com.example.myhealthapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {


    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvLogin;
    private Button bSignUp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvLogin = findViewById(R.id.tvLogin);
        bSignUp = findViewById(R.id.ButtonSignUp);
        progressBar= findViewById(R.id.progressBar);



        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
            finish();

        }


        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String name = etFullName.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    etEmail.setError("Please enter your Email");
                    return;
                }


                if(TextUtils.isEmpty(pass))
                {
                    etPassword.setError("Please enter your Password");
                    return;
                }


                if (pass.length()<6)
                {
                    etPassword.setError("pass must be of atleast 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity1.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Error occured"/*+ task.getException().getMessage()*/, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });



                tvLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });



            }
});

    }
}