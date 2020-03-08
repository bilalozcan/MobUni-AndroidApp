package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.AnswersActivity;
import com.fmbg.moobuni.Model.Question;
import com.fmbg.moobuni.Model.User;
import com.fmbg.moobuni.ProfileOtherActivity;
import com.fmbg.moobuni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    public Context mContext;
    public List<Question> mQuestion;
    private FirebaseUser firebaseUser;

    public QuestionAdapter(Context mContext, List<Question> mQuestion) {
        this.mContext = mContext;
        this.mQuestion = mQuestion;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_item,parent, false);
        return new QuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Question question = mQuestion.get(position);
        holder.question.setText(question.getQuestion());
        publisherInfo(holder.user_logo, holder.user_name, question.getPublisher());
        getAnsers(question.getQuestionid(), holder.answers);
        holder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AnswersActivity.class);
                intent.putExtra("questionid", question.getQuestionid());
                intent.putExtra("publisher", question.getPublisher());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AnswersActivity.class);
                intent.putExtra("questionid", question.getQuestionid());
                intent.putExtra("publisher", question.getPublisher());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.university.setText(question.getUniversity());
        if (question.getDepartment().equals("all")){
            holder.department.setText("Genel");
        }else {
            holder.department.setText(question.getDepartment());
        }
        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileOtherActivity.class);
                intent.putExtra("userid", question.getPublisher());
                mContext.startActivity(intent);
            }
        });
        holder.user_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileOtherActivity.class);
                intent.putExtra("userid", question.getPublisher());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mQuestion.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, question, answers, university, department,answer;
        public ImageView user_logo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_logo = itemView.findViewById(R.id.user_logo);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            answers = itemView.findViewById(R.id.answers);
            university = itemView.findViewById(R.id.question_user_university);
            department = itemView.findViewById(R.id.question_user_department);

        }
    }

    private void getAnsers(String questionid, final TextView answers) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Answers").child(questionid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                answers.setText(dataSnapshot.getChildrenCount() + " Cevabı Gör");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void publisherInfo(final ImageView image_profile, final TextView username ,String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
