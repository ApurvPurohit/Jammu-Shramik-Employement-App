package com.example.android.hrm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Employer_requirement extends AppCompatActivity {
    Button create_work;
    Spinner job;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAv1HH8GU:APA91bGf3Xv_L8vj0AzoRkrfXr4CzTX5sc6MyGfDp9CWEeLbFF1QIYpY8lYRFik6AsaHdp8lrjL-QCcK-tUU0rrq8nHK7DFT6fsLlDvVbbmEFQW-5bxO33ix_lQwhqGbVZtXwQYyj55m";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC,Name,Phn;
    EditText number_days,number_labourer,job_description,dummy;
    FirebaseDatabase rootNode,j;
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
        String n= getIntent().getStringExtra("EmployerName");
        String p = getIntent().getStringExtra("EmployerPhone");
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
                    //SENDING THE PUSH NOTIFICATION TO THE TOPIC
                    MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService((job.getSelectedItemPosition()));
                    TOPIC = "/topics/"+(job.getSelectedItemPosition()); //topic has to match what the receiver subscribed to
                    NOTIFICATION_TITLE = "आपके पास रोजगार का एक अवसर है";
                    NOTIFICATION_MESSAGE = "हमने आपके काम से मेल खाते हुए एक काम पाया है";

                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    sendNotification(notification);
                }
            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        com.example.android.hrm.MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void sendData()
    {
        String job_required=(job.getSelectedItem()).toString();
//        SendNotif notif = new SendNotif(job_required,"नौकरी उपलब्ध है","हमने आपके काम से मेल खाते हुए नौकरी पाई है");
//        notif.getEmpTokens();
        String no_days=number_days.getText().toString();
        String no_labourer=number_labourer.getText().toString();
        String job_desp=job_description.getText().toString();
        String n=getIntent().getStringExtra("EmployerName");
        String p=getIntent().getStringExtra("EmployerPhone");
        Calendar currenttime=Calendar.getInstance();
        String date= DateFormat.getDateInstance().format(currenttime.getTime());
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Employer_Work");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userid=user.getUid();
        EmployerRequirementHelper helperClass = new EmployerRequirementHelper(no_days,no_labourer,job_desp,job_required,date);
        reference.child(userid).setValue(helperClass);
        DatabaseReference reference1 = rootNode.getReference("Employer_Work_History");
        Date currentTime = Calendar.getInstance().getTime();
        reference1.child(userid).child(userid+currentTime.toString()).setValue(helperClass);

        Toast.makeText(this, "काम सफलतापूर्वक दर्ज किया गया", Toast.LENGTH_LONG).show();
        j=FirebaseDatabase.getInstance();
        DatabaseReference reff=j.getReference("job");
        EmployerJOBDetailsHelper hclas=new EmployerJOBDetailsHelper(no_days,no_labourer,job_desp,n,p,date);
        reff.child(job_required).child(userid+currentTime.toString()).setValue(hclas);

    }


}

