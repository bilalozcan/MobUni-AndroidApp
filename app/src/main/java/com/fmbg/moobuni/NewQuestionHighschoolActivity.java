package com.fmbg.moobuni;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/*editText = findViewById(R.id.question_added_highschool);
editText.setHint("İstanbul Medeniyet Üniversitesi Bilgisayar Mühendisliğine Soru Sorunuz");*/
public class NewQuestionHighschoolActivity extends AppCompatActivity {

    ImageView close;
    EditText question;
    TextView share;
    String university_name;
    String department_name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question_highschool);
        close = findViewById(R.id.close_highschool);
        question = findViewById(R.id.question_added_highschool);
        share = findViewById(R.id.share_highschool);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        question.getText().toString();

        university_name = sharedPreferences.getString("university_name", university_name);
        department_name = sharedPreferences.getString("department_name", department_name);

        question.setHint(university_name + "\n\n" + department_name + "\n\nBölümüne Soru Sormaktasınız");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(NewQuestionHighschoolActivity.this, HighschoolMainActivity.class));
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQuestion();
            }
        });
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
                    hashMap.put("university", university_name);
                    hashMap.put("department", department_name);

                    reference.child(questionId).setValue(hashMap);
                    progressDialog.dismiss();
                    startActivity(new Intent(NewQuestionHighschoolActivity.this, HighschoolMainActivity.class));
                    finish();
        } else {
            Toast.makeText(this, "Lütfen Soru Sorma Alanını Doldurunuz", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
