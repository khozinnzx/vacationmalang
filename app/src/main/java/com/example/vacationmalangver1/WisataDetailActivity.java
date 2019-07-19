package com.example.vacationmalangver1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class WisataDetailActivity extends AppCompatActivity {

    private ImageView ImageWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_detail);

        //get data

        String namaTempat = getIntent().getExtras().getString("nama tempat");
        int imageWisata = getIntent().getExtras().getInt("image");
        ImageWisata = findViewById(R.id.img_detail_wisata);
        ImageWisata.setImageResource(imageWisata);
    }
}
