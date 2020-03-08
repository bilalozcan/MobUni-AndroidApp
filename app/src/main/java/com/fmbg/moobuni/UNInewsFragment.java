package com.fmbg.moobuni;


import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbg.moobuni.Adapter.DuyuruAdapter;
import com.fmbg.moobuni.Model.Duyuru;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UNInewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView news_title;
    private DuyuruAdapter duyuruAdapter;
    private List<Duyuru> duyuruList;
    private List<Duyuru> duyuruListAgain;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uninews, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        news_title = view.findViewById(R.id.news_title);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        duyuruList = new ArrayList<>();
        duyuruListAgain = new ArrayList<>();
        duyuruAdapter = new DuyuruAdapter(getContext(), duyuruListAgain);
        recyclerView.setAdapter(duyuruAdapter);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getUid()).child("university").getValue().toString().equals("İstanbul Medeniyet Üniversitesi")){
                    readNews();
                } else {
                    news_title.setVisibility(View.VISIBLE);
                    news_title.setText("Yakında Üniversitenizin Haberleri Eklenecektir. Beklemede Kalın");
                    news_title.setTextSize(35);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    private void readNews() {
        HashMap<String, String> hashMap;
        try {
            Document document = Jsoup.connect("https://www.medeniyet.edu.tr/tr/duyurular").get();
            Elements document2 = document.select(".vertical");
            Elements document3 = document2.select("div[class=col-sm-4]");
                       duyuruList.clear();
            for(Element element : document3) {
                hashMap = new HashMap<String, String>();
                hashMap.put("day", element.getElementsByClass("day").text());
                hashMap.put("date", element.getElementsByClass("month").text());
                hashMap.put("title", element.getElementsByClass("content-img stretch").text());
                hashMap.put("url", element.getElementsByTag("a").attr("href").toString());
                Duyuru duyuru = new Duyuru(hashMap.get("day"), hashMap.get("date"), hashMap.get("title"), hashMap.get("url"));
                duyuruList.add(duyuru);
            }
            for(int i = duyuruList.size()-1 ; i >= 0 ; --i){
                duyuruListAgain.add(duyuruList.get(i));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        duyuruAdapter.notifyDataSetChanged();
    }
}