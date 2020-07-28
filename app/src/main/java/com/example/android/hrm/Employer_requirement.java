package com.example.android.hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Employer_requirement extends AppCompatActivity {
    Button create_work;
    Spinner job;
    EditText number_days,number_labourer,job_description,dummy;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_requirement);
        number_days= findViewById(R.id.number_of_days);
        number_labourer= findViewById(R.id.number_of_labourer);
        job_description = findViewById(R.id.job_description);
        dummy = findViewById(R.id.address);
        dummy.setText(getIntent().getStringExtra("addr"));
        create_work= findViewById(R.id.create_work);
        job= findViewById(R.id.spinner_employer_need);
        findViewById(R.id.button222).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        create_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                String nd=number_days.getText().toString();
                String nl=number_labourer.getText().toString();
                String ss=job_description.getText().toString();
                String j=(job.getSelectedItem()).toString();
                if(j.equals("काम चुने"))
                {
                    Toast.makeText(getApplicationContext(), "काम चुनें", Toast.LENGTH_SHORT).show();
                    job.requestFocus();
                }
                else if(nd.length()==0 || Integer.parseInt(nd)>20)
                {
                    number_days.setError("1-20 की सीमा के भीतर दर्ज करें");
                    number_days.requestFocus();
                }
                else if(nl.length()==0 || Integer.parseInt(nl)>20)
                {
                    number_labourer.setError("1-20 की सीमा के भीतर दर्ज करें");
                    number_labourer.requestFocus();
                }
                else if(ss.length()>60||ss.isEmpty())
                {
                    job_description.setError("1 से 60 शब्दों के बीच दर्ज करें");
                    job_description.requestFocus();
                }
                else
                {
                    sendData();
                }
            }
        });
    }

    public void sendData()
    {
        String no_days=number_days.getText().toString();
        String no_labourer=number_labourer.getText().toString();
        String job_desp=job_description.getText().toString();
        String job_required=(job.getSelectedItem()).toString();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Employer_Work");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userid=user.getUid();
        Employer_requirement_send helperClass = new Employer_requirement_send(no_days,no_labourer,job_desp,job_required);
        reference.child(userid).setValue(helperClass);
        DatabaseReference reference1 = rootNode.getReference("Employer_Work_History");
        Date currentTime = Calendar.getInstance().getTime();
        reference1.child(userid).child(userid+currentTime.toString()).setValue(helperClass);
        Toast.makeText(this, "काम सफलतापूर्वक दर्ज किया गया", Toast.LENGTH_LONG).show();
    }

}

