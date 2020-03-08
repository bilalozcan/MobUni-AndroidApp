package com.fmbg.moobuni.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Model.Department;
import com.fmbg.moobuni.NewQuestionUniversityActivity;
import com.fmbg.moobuni.R;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder>{
    public Context mContext;
    public List<Department> departmentList;

    public DepartmentAdapter(Context mContext, List<Department> departmentList) {
        this.mContext = mContext;
        this.departmentList = departmentList;
    }


    @NonNull
    @Override
    public DepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.department_item, parent, false);
        return new DepartmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentAdapter.ViewHolder holder, int position) {
        final Department department = departmentList.get(position);
        holder.item_select.setText(department.getName());
        holder.item_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewQuestionUniversityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("department_name", department.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_select = itemView.findViewById(R.id.department_name);
        }
    }
}
