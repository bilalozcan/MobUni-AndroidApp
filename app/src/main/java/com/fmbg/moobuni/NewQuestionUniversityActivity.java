package com.fmbg.moobuni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fmbg.moobuni.Adapter.UniversityAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class NewQuestionUniversityActivity extends AppCompatActivity {

    StorageTask uploadTask;

    ImageView close;
    EditText question;
    TextView share;
    Button department_name;
    TextView university_name;
    String universityname = null;
    String departmentname = null;
    String universityid = null;
    private FirebaseAuth currentAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question_university);

        close = findViewById(R.id.close);
        question = findViewById(R.id.question_added);
        share = findViewById(R.id.share);
        university_name = findViewById(R.id.university_name_2);
        department_name = findViewById(R.id.department_name_2);

        currentAuth=FirebaseAuth.getInstance();

        getUniversityName();

        question.getText().toString();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(NewQuestionUniversityActivity.this, UniversityMainActivity.class));
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQuestion();
            }
        });

        department_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewQuestionUniversityActivity.this, DepartmentSelectActivity.class);
                intent.putExtra("university_id", UniversityAdapter.university_id);
                intent.putExtra("university_name", universityname);
                startActivity(intent);
            }
        });
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            departmentname = intent.getString("department_name");
            if(departmentname != null)
                department_name.setText(departmentname);

            universityid = intent.getString("university_id");
        }




    }
    private void shareQuestion(){

        question.getText().toString();
        String question2 = question.getText().toString();
        if(!(question2.isEmpty())){
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Sharing");
                    progressDialog.show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");

                    String questionId = reference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("questionid", questionId);
                    hashMap.put("question", question2);
                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("university", university_name.getText().toString());
                    if(department_name.getText().toString().equals("Genel")){
                        hashMap.put("department", "all");
                    } else {

                        hashMap.put("department", department_name.getText().toString());
                    }

                    reference.child(questionId).setValue(hashMap);
                    progressDialog.dismiss();
                    startActivity(new Intent(NewQuestionUniversityActivity.this, UniversityMainActivity.class));
                    finish();
        } else {
            Toast.makeText(this, "Lütfen Soru Sorma Alanını Doldurunuz", Toast.LENGTH_SHORT).show();
        }
    }
    private void getUniversityName() {
        final FirebaseUser user =currentAuth.getCurrentUser();
        if(user != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    universityname = dataSnapshot.child(user.getUid()).child("university").getValue().toString();
                    university_name.setText(universityname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


}
