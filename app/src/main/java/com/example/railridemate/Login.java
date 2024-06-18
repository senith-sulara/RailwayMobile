package com.example.railridemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {


    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://trainadmin-c8c98-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.phone);
        final EditText password =findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneTxt=phone.getText().toString();
                final String passwordTxt=password.getText().toString();

                if(phoneTxt.isEmpty()|| passwordTxt.isEmpty()){
                    Toast.makeText(Login.this,"Please enter your mobile or password",Toast.LENGTH_SHORT).show();
                }else{

                    databaseReference.child("usersC").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean userFound = false;
                            String userKey = null;
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String username = userSnapshot.child("username").getValue(String.class);
                                String password = userSnapshot.child("password").getValue(String.class);
                                if (phoneTxt.equals(username) && passwordTxt.equals(password)) {
                                    userFound = true;
                                    userKey = userSnapshot.getKey(); // Store the key of the matching user
                                    break;
                                }
                            }
                            if(snapshot.hasChild(phoneTxt))   {


                            final String getPassword=snapshot.child(phoneTxt).child("password").getValue(String.class);

                                if (userFound && userKey != null) {
                                Toast.makeText(Login.this,"Successfully Loggin In",Toast.LENGTH_SHORT).show();

                                // Fetch the username
                                final String username = snapshot.child(phoneTxt).child("username").getValue(String.class);
                             //   final  String userKey = snapshot.child(phoneTxt).getKey();
                                // Pass username to MainActivity
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("username", username);
                                intent.putExtra("userKey", userKey);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Login.this,"Wrong Password",Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            Toast.makeText(Login.this,"Wrong Password",Toast.LENGTH_SHORT).show();

                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Open Refister action
                startActivity(new Intent(Login.this,Register.class));

            }
        });
    }
}