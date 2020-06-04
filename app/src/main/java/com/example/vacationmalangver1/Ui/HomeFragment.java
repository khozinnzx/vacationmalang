package com.example.vacationmalangver1.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.vacationmalangver1.Adapter.RvWisataAdapter;
import com.example.vacationmalangver1.Model.DataTempatWisata;
import com.example.vacationmalangver1.R;
import com.example.vacationmalangver1.Ui.WisataDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements RvWisataAdapter.OnClickRvListener {


    RecyclerView recyclerView;
    List<DataTempatWisata> mList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View fragmenView = inflater.inflate(R.layout.fragment_home, container, false);
        //1. get reference to recycleview
        recyclerView = (RecyclerView) fragmenView.findViewById(R.id.rv_wisata_list);

        //2. set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //this is data recyclerview
        mList = new ArrayList<>();
        mList.add(new DataTempatWisata(R.drawable.balekambang, "Pantai Balekambang", "Dusun Sumber Jambe Desa Srigonco", getString(R.string.deskripsi_balekambang), "Rp.10.000 - Rp.15.000"));
        mList.add(new DataTempatWisata(R.drawable.sumbermaron, "Sumber Maron", "Karangsuko, Pagelaran, Dusun Adi Luwih, Karangsuko",getString(R.string.deskripsi_sumbermaron) ,"Rp.3.000"));
        mList.add(new DataTempatWisata(R.drawable.cobanrondo, "Air Terjun Coban Rondo", "Jalan Coban Rondo, Krajan, Pandesari, Pujon", getString(R.string.deskripsi_cobanrondo), "Rp.30.000(Domestik), Rp.35.000(Mancanegara)"));
        mList.add(new DataTempatWisata(R.drawable.jatimpark2, "Jawa Timur Park II", "Jl. Oro-Oro Ombo No.9, Temas, Kec. Batu, Kota Batu", getString(R.string.deskripsi_jatimpark2), "Rp.125.000(Weekend), Rp.95.000(Weekday)"));
        mList.add(new DataTempatWisata(R.drawable.omahkayu, "Omah Kayu", "Jl. Gn. Banyak, Gunungsari, Bumiaji, Kota Batu", getString(R.string.deskripsi_omahkayu), "Rp.10.000"));
        mList.add(new DataTempatWisata(R.drawable.museumangkut,"Muserum Angkut","Jl.Terusan Sultan Agung No.2, Ngaglik, Kota Batu, Jawa Timur",getString(R.string.deskripsi_museumangkut),"Rp.70.000(Weekday), Rp.80.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.pantaitigawarna, "Pantai Tiga Warna", "Jl. Sendang Biru, Area Sawah/Kebun, Tambakrejo, Sumbermanjing", getString(R.string.deskripsi_tigawarna), "Rp.6000/orang, sewa guide Rp.100.000"));
        mList.add(new DataTempatWisata(R.drawable.goacina, "Pantai Goa Cina", "Tambak, Sitiarjo, Sumbermanjing", getString(R.string.deskripsi_goacina), "Rp.5000"));
        mList.add(new DataTempatWisata(R.drawable.predatorfunpark, "Predator Fun Park", "l. Raya Tlekung No. 315, Dusun Gangsiran, Desa Tlekung, Kecamatan Junrejo, Kota Batu, Jawa Timur ", getString(R.string.deskripsi_predatorfunpark), "Rp.35.000(Weekday), Rp.50.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.paralayang, "Wisata Paralayang", "Jl, Songgokerto, Kec. Batu, Kota Batu", getString(R.string.deskripsi_paralayang), "Rp.10.000(Weekday), Rp.15.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.hawaii, "Hawaii Waterpark", "Perumahan Graha Kencana Raya, Jl. Raya Karanglo, Singosari", getString(R.string.deskripsi_hawaiiwaterpark), "Rp.75.000(Weekday), Rp.100.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.bns, "Batu Night Spectacular", "Jalan Hayam Wuruk No. 1, Oro-Oro Ombo, Kec. Batu Kota, Jawa Timur 65316, Indonesia", getString(R.string.deskripsi_bns), "Rp.60.000 (Allday Paket), Rp.30.000 (Senin – Jumat), Rp.40.000 (Sabtu – Minggu)"));
        mList.add(new DataTempatWisata(R.drawable.tamanselecta, "Taman Selecta", "Jl. Raya Selecta No. 1, Bumiaji, Tulungrejo, Kota Batu, Jawa Timur 65336", getString(R.string.deskripsi_selecta), "Rp.35.000"));
        mList.add(new DataTempatWisata(R.drawable.banyuanjlok, "Banyu Anjlok", "Desa Purwodadi, Tirtoyudo, Kabupaten Malang, Jawa Timur, Indonesia", getString(R.string.deskripsi_banyuanjlok), "Rp.10.000 per orang"));
        mList.add(new DataTempatWisata(R.drawable.jodipan, "kampung warna warni jodipan", "Kec. Blimbing, Kota Malang, Jawa Timur", getString(R.string.deskripsi_jodipan), "Rp.3.000"));
        mList.add(new DataTempatWisata(R.drawable.jatimpark1, "Jawa Timur Park 1", "Jl. Kartika No.2, Sisir, Kec. Batu, Kota Batu, Jawa Timur 65315", getString(R.string.deskripsi_jatimpark1), "Rp.60.000(Weekday), Rp.80.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.pantaingliyep, "Pantai Ngliyep", "Desa, Hutan, Kedungsalam, Donomulyo, Malang, Jawa Timur 65167", getString(R.string.deskripsi_pantaingliyep), "Rp.5000"));
        mList.add(new DataTempatWisata(R.drawable.batuwonderland, "Batu Wonderland", "Jl. Imam Bonjol No.9, Temas, Kec. Batu, Kota Batu, Jawa Timur 65315", getString(R.string.deskripsi_batuwonderland), "Rp.10.000(Weekday), Rp.20.000(Weekend)"));
        mList.add(new DataTempatWisata(R.drawable.museumbrawijaya, "Museum Brawijaya", "Jl. Ijen No.25 A, Gading Kasri, Kec. Klojen, Kota Malang, Jawa Timur 65115", getString(R.string.deskripsi_museumbrawijaya), "Rp.3.000(anak), Rp.5.000(dewasa)"));
        mList.add(new DataTempatWisata(R.drawable.cobantalun, "Coban Talun", "Dusun Wonorejo, Desa, Tulungrejo, Kec. Bumiaji, Kota Batu, Jawa Timur 65336", getString(R.string.deskripsi_cobantalun), "Rp.10.000"));





        //3. create an adapter
        RvWisataAdapter adapter = new RvWisataAdapter( mList, this);
        recyclerView.setAdapter(adapter);

        runAnimation(recyclerView, 0);

        return fragmenView;
    }

    private void runAnimation(RecyclerView recyclerView, int type) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall);

        //set anim
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    public void onClickRv(int position) {

        Intent intent = new Intent(getActivity(), WisataDetailActivity.class);
        intent.putExtra("NAMA_TEMPAT", mList.get(position).getNamaTempat());
        intent.putExtra("IMAGE", mList.get(position).getBackground());
        intent.putExtra("ALAMAT", mList.get(position).getAlamatTempat());
        intent.putExtra("HARGA_TIKET", mList.get(position).getHargaTiket());
        intent.putExtra("DESKRIPSI", mList.get(position).getDeskripsi());

        startActivity(intent);

    }
}
