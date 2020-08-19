package com.example.android.hrm;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmployerRegistration extends AppCompatActivity {

    Button save,home;
    EditText Name, Phone, Address;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer);
        Name = findViewById(R.id.editTextTextPersonName2);
        Phone = findViewById(R.id.editTextPhone2);
        Address = findViewById(R.id.editTextAddress2);
        save=findViewById(R.id.button4);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                String name = Name.getText().toString();
                String phone = Phone.getText().toString();
                String adr = Address.getText().toString();
                if(name.isEmpty())
                {
                    Name.setError("Name Required");
                    Name.requestFocus();
                }
                else if(phone.length() != 10)
                {
                    Phone.setError("Invalid Phone Number");
                    Phone.requestFocus();
                }
                else if(adr.isEmpty())
                {
                    Address.setError("Address required");
                    Address.requestFocus();
                }
                else
                {
                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference;
                    databaseReference =db.getReference("Employer");
                    EmployerRegistrationHelper employer = new EmployerRegistrationHelper(name,phone,adr);
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    String userid = user.getUid();
                    databaseReference.child(userid).setValue(employer);
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    i.putExtra("name",name);
                    i.putExtra("phone",phone);
                    i.putExtra("occ",adr);
                    i.putExtra("stat","नियोक्ता");
                    i.putExtra("available","1");
                    startActivity(i);
                }
            }
        });
    }
}
