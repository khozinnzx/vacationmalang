package com.example.vacationmalangver1.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vacationmalangver1.R;

public class WisataDetailActivity extends AppCompatActivity {

    ImageView imgPhoto;
    TextView namaWisata, alamat, hargaTiket, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_detail);

//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getSupportActionBar().hide();

        imgPhoto = findViewById(R.id.img_photo);
        namaWisata = findViewById(R.id.tv_nama);
        alamat = findViewById(R.id.tv_alamat);
        hargaTiket = findViewById(R.id.tv_harga_tiket);
        deskripsi = findViewById(R.id.tv_deskripsi);

        int postPhoto = getIntent().getExtras().getInt("IMAGE");
        imgPhoto.setImageResource(postPhoto);
        String postNamaWisata = getIntent().getExtras().getString("NAMA_TEMPAT");
        namaWisata.setText(postNamaWisata);
        String postAlamat = getIntent().getExtras().getString("ALAMAT");
        alamat.setText(postAlamat);
        String postHargaTiket = getIntent().getExtras().getString("HARGA_TIKET");
        hargaTiket.setText(postHargaTiket);
        String postDeskripsi = getIntent().getExtras().getString("DESKRIPSI");
        deskripsi.setText(postDeskripsi);
    }
}
