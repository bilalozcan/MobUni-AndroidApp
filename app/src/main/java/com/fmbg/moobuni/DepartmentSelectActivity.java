package com.fmbg.moobuni;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Adapter.DepartmentAdapter;
import com.fmbg.moobuni.Model.Department;
import com.fmbg.moobuni.Model.University;

import java.util.ArrayList;
import java.util.List;

public class DepartmentSelectActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    private List<Department> departmentList;
    private DepartmentAdapter departmentAdapter;
    String universityname = null;
    String universityid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_select);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bölüm Seç");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        departmentList = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(getApplicationContext(), departmentList);
        recyclerView.setAdapter(departmentAdapter);
        Bundle intent = getIntent().getExtras();
        if (intent != null){
            universityname = intent.getString("university_name");
        }

        readDepartment();
    }

    private void readDepartment() {
        XMLParserUniversity xmlParserUniversity = new XMLParserUniversity(getApplicationContext());
        final ArrayList<University> universityArrayList = xmlParserUniversity.ParsUniversity();
        for(int i = universityArrayList.size() - 1; i >= 0; i--){
            if(universityArrayList.get(i).getName().equals(universityname))
                universityid = universityArrayList.get(i).getUniversite_id();
        }

        XMLParserDepartment xmlParserDepartment = new XMLParserDepartment(getApplicationContext());
        final ArrayList<Department> departmentArrayList = xmlParserDepartment.ParsDepartment();
        for(int i = departmentArrayList.size() - 1; i >= 0; i--){
            if(departmentArrayList.get(i).getUniversite_id().equals(universityid))
            departmentList.add(departmentArrayList.get(i));
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
