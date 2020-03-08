package com.fmbg.moobuni;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Adapter.QuestionAdapter;
import com.fmbg.moobuni.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserQuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private List<Question> questionsList;
    private String userid;
    private TextView emptyQuestionWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_questions);

        Bundle intent = getIntent().getExtras();
        if(intent != null) {
            userid = intent.getString("userid");
        }
        emptyQuestionWarning = findViewById(R.id.emptyQuestionWarning);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        questionsList =new ArrayList<>();
        if(questionsList.size() == 0){
            emptyQuestionWarning.setVisibility(View.VISIBLE);
            emptyQuestionWarning.setText("Hiç Soru Sormamışsın Hemen Soru Sor!");
        }
        questionAdapter = new QuestionAdapter(getApplicationContext(), questionsList);
        recyclerView.setAdapter(questionAdapter);
        readQuestions();
    }
    private void readQuestions(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Question question = snapshot.getValue(Question.class);
                    if(question.getPublisher().equals(userid)){
                        questionsList.add(question);
                    }
                }
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
