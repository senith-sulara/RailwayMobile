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

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://trainadmin-c8c98-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText Username=findViewById(R.id.username);

        final EditText password=findViewById(R.id.regiPassword);
        final EditText confirmPassword=findViewById(R.id.confirm_password);

        final Button regButon=findViewById(R.id.regiserBtn);
        final TextView loginNowBtn = findViewById(R.id.lgNowBtn);

regButon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        //ge data form editText into String variable
        final String usernameTxt= Username.getText().toString();

        final String passwordTxt=password.getText().toString();
        final String conPasswordTxt=confirmPassword.getText().toString();

        //check if user fill all the fields before sending data to firebase
        if(usernameTxt.isEmpty() || passwordTxt.isEmpty() || passwordTxt.isEmpty()){
            Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else if(!passwordTxt.equals(conPasswordTxt)){
            Toast.makeText(Register.this,"Password are not matching",Toast.LENGTH_SHORT).show();
        }
        else {
            databaseReference.child("usersC").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(usernameTxt)){
                        Toast.makeText(Register.this,"UserD Name or Phone is Already",Toast.LENGTH_SHORT).show();

                    }else{
                        databaseReference.child("usersC").child(usernameTxt).child("username").setValue(usernameTxt);
                   //     databaseReference.child("users").child(usernameTxt).child("phone").setValue(phoneTxt);
                        databaseReference.child("usersC").child(usernameTxt).child("password").setValue(passwordTxt);

                        Toast.makeText(Register.this,"UserD Registered Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,Login.class));
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }
});
//
loginNowBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
});
    }
}