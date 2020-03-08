package com.fmbg.moobuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Adapter.AnswerAdapter;
import com.fmbg.moobuni.Model.Answer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnswerAdapter answerAdapter;
    private List<Answer> answerList;

    EditText addanswer;
    TextView answer;
    String questionid, publisherid;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cevaplar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        answerList = new ArrayList<>();
        answerAdapter = new AnswerAdapter(this, answerList);
        recyclerView.setAdapter(answerAdapter);

        addanswer = findViewById(R.id.add_answer);
        answer = findViewById(R.id.answer);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        questionid = intent.getStringExtra("questionid");
        publisherid = intent.getStringExtra("publisherid");

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addanswer.getText().toString().equals("")) {
                    Toast.makeText(AnswersActivity.this, "Boş Cevap Yazamazsınız!", Toast.LENGTH_SHORT).show();
                } else {
                    addAnswer();
                }
            }
        });


        readAnswers();

    }
    private void addAnswer(){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Answers").child(questionid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("answer", addanswer.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        reference.push().setValue(hashMap);

        addanswer.setText("");
    }/*
    private void getImage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/
    private void readAnswers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Answers").child(questionid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                answerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Answer answer = snapshot.getValue(Answer.class);
                    answerList.add(answer);
                }
                answerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}