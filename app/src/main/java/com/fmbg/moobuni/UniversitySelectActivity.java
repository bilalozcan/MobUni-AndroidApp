package com.fmbg.moobuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Adapter.UniversityAdapter;
import com.fmbg.moobuni.Model.University;

import java.util.ArrayList;
import java.util.List;

public class UniversitySelectActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Button button_filter_successful;
    private List<University> universityList;
    private UniversityAdapter universityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_select);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Üniversite Seç");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_filter_successful = findViewById(R.id.button_filter_successful);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(200);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        universityList = new ArrayList<>();
        universityAdapter = new UniversityAdapter(getApplicationContext(), universityList);
        recyclerView.setAdapter(universityAdapter);

        readUniversity();

        button_filter_successful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UniversitySelectActivity.this, UniversityMainActivity.class));
            }
        });

    }

    private void readUniversity() {
        XMLParserUniversity xmlParserUniversity = new XMLParserUniversity(getApplicationContext());
        final ArrayList<University> universityArrayList = xmlParserUniversity.ParsUniversity();
        for (int i = universityArrayList.size() - 1 ; i >= 0 ; i--){
            universityList.add(universityArrayList.get(i));
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
