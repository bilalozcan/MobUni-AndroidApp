package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fmbg.moobuni.Model.University;
import com.fmbg.moobuni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {
    public Context mContext;
    public List<University> universityList;
    public static String university_name= "";
    public static String university_id= "";

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    public UniversityAdapter(Context mContext, List<University> universityList) {
        this.mContext = mContext;
        this.universityList = universityList;
    }

    @NonNull
    @Override
    public UniversityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.university_item, parent, false);
        return new UniversityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UniversityAdapter.ViewHolder holder, int position) {
        final University university = universityList.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Filter University").child(firebaseUser.getUid());

        holder.university_name.setText(university.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.university_checkbox.isChecked()){
                    holder.university_checkbox.setChecked(false);
                } else {
                    holder.university_checkbox.setChecked(true);
                }

            }
        });
        holder.university_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String universityId = reference.push().getKey();
                if(holder.university_checkbox.isChecked()){
                    reference.child(university.getUniversite_id()).setValue(university);
                    reference.child(university.getUniversite_id()).child("checked").setValue(true);
                } else {
                    reference.child(university.getUniversite_id()).setValue(university);
                    reference.child(university.getUniversite_id()).child("checked").setValue(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return universityList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView university_name;
        public CheckBox university_checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            university_name = itemView.findViewById(R.id.item_select);
            university_checkbox = itemView.findViewById(R.id.university_checkbox);
        }
    }
}
