package com.fmbg.moobuni;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    private TextView aboutText;
    Toolbar toolbarAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarAbout = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbarAbout);
        setSupportActionBar(toolbarAbout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_about);
        aboutText = findViewById(R.id.about_textview);
        aboutText.setText("MobUni uygulaması programlamaya yeni başlamış 3 arkadaş Mehmet Hafif ARSAY ,Bilal ÖZCAN ve Fatih GAYBERİ tarafından; üniversite öğrencilerinin birbirleri ile iletişim kurmasını, kulüp etkinliklerinin duyurulması, okuduğu üniversitenin son dakika duyurularına erişmesi için tasarlanmıştır.\n" +
                "        Uygulama iki kısımdan oluşmaktadır. 1. Kısıma üniversite ogrencilerinin yararlanması 2. Kısıma da ise üniversiteye geçecek olan gençlerin merak ettikleri üniversite hakkında 1. kişi tarafından bilgilendirmesinden olusur. Uygulama şu anda geliştirme aşamasında olup hatalar tespit sonucu guncellemeler alacaktır .\n" +
                "        Uygulama kullanıcısı tarafından paylaşılan verilerin sorumluluğu kullanıcıya aitir. Veriler paylaşıldıktan sonra kullanım hakları mobuni company'e aittir.");
    }
}
