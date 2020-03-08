package com.fmbg.moobuni;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class HighSchoolQuestionFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private List<Question> questionsList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String university_name;
    String department_name;


    TextView universityname, departmentname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schschools, container, false);


        universityname = view.findViewById(R.id.university_name_question_fragment);
        departmentname = view.findViewById(R.id.department_name_question_fragment);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        university_name = sharedPreferences.getString("university_name", university_name);

        department_name = sharedPreferences.getString("department_name", department_name);

        questionsList =new ArrayList<>();
        questionAdapter = new QuestionAdapter(getContext(), questionsList);
        recyclerView.setAdapter(questionAdapter);

        universityname.setText(university_name);
        departmentname.setText(department_name);

        readQuestions();
        return view;
    }
    private void readQuestions(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Question question = snapshot.getValue(Question.class);
                    if(question.getUniversity().equals(university_name) && question.getDepartment().equals(department_name) ){
                        questionsList.add(question);
                    }
                    if(question.getUniversity().equals(university_name) && question.getDepartment().equals("all")){
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
