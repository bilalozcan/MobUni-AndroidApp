package com.fmbg.moobuni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fmbg.moobuni.Model.Department;
import com.fmbg.moobuni.Model.University;

import java.util.ArrayList;

public class HighschoolSelectUniversityActivity extends AppCompatActivity {

    private Spinner spinner_university, spinner_department;
    private Toolbar toolbar;
    private Button button;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ArrayAdapter<String> dataAdapterForUniversiteler;
    private ArrayAdapter<String> dataAdapterForBolumler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highschool_select_university);

        toolbar = findViewById(R.id.select_university_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Üniversite ve Bölüm Seçiniz");

        spinner_university = findViewById(R.id.spinner_highschool_university);
        spinner_department = findViewById(R.id.spinner_highschool_department);
        button = findViewById(R.id.button_select_university);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spinnerOlusturma();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                editor.putString("university_name", spinner_university.getSelectedItem().toString());
                editor.putString("department_name", spinner_department.getSelectedItem().toString());
                editor.commit();
                Intent intent = new Intent(HighschoolSelectUniversityActivity.this, HighschoolMainActivity.class);
                intent.putExtra("intent", true);
                startActivity(intent);
                finish();
            }
        });

    }

    private void spinnerOlusturma() {
        final ArrayList<String> nameList = new ArrayList<String>();
        final ArrayList<String> departmentList = new ArrayList<String>();

        XMLParserUniversity xmlParserUniversity = new XMLParserUniversity(getApplicationContext());
        final ArrayList<University> universityArrayList = xmlParserUniversity.ParsUniversity();

        XMLParserDepartment xmlParserDepartment = new XMLParserDepartment(getApplicationContext());
        final ArrayList<Department> departmentArrayList = xmlParserDepartment.ParsDepartment();

        for(int i = 0 ; i < universityArrayList.size(); ++i){
            nameList.add(universityArrayList.get(i).getName());
        }
        for (int i = 0; i < departmentArrayList.size(); ++i) {
            if (departmentArrayList.get(i).getUniversite_id().equals("1")){
                departmentList.add(departmentArrayList.get(i).getName());
            }
        }

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForUniversiteler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
        dataAdapterForBolumler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departmentList);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForUniversiteler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForBolumler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinner_university.setAdapter(dataAdapterForUniversiteler);
        spinner_department.setAdapter(dataAdapterForBolumler);

        // Listelerden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinner_university.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Hangi il seçilmişse onun ilçeleri adapter'e ekleniyor.
                String universite_id = "";
                for (int i = 0; i < universityArrayList.size(); i++) {
                    if(parent.getSelectedItem().toString().equals(universityArrayList.get(i).getName())){
                        universite_id = universityArrayList.get(i).getUniversite_id();
                    }
                }
                departmentList.clear();
                for (int j = 0; j < departmentArrayList.size(); j++) {
                    if(departmentArrayList.get(j).getUniversite_id().equals(universite_id)){
                        departmentList.add(departmentArrayList.get(j).getName());
                    }
                }
                dataAdapterForBolumler = new ArrayAdapter<String>(HighschoolSelectUniversityActivity.this, android.R.layout.simple_spinner_item, departmentList);
                dataAdapterForBolumler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_department.setAdapter(dataAdapterForBolumler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Seçilen il ve ilçeyi ekranda gösteriyoruz.
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
