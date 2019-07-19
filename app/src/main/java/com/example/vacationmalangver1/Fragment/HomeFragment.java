package com.example.vacationmalangver1.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vacationmalangver1.Adapter.RvWisataAdapter;
import com.example.vacationmalangver1.Model.DataTempatWisata;
import com.example.vacationmalangver1.R;
import com.example.vacationmalangver1.WisataDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ImageItemClickListener {


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
        List<DataTempatWisata> mList = new ArrayList<>();
        mList.add(new DataTempatWisata(R.drawable.jodipan, "kampung warna warni jodipan", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.balekambang, "pantai balekambang", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.banyuanjlok, "pantai banyu anjlok", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.goacina, "pantai goa cina", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.hawaii, "hawaii waterpark", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.museumangkut, "museum angkut", "jl.candi Vi A nomer 60"));
        mList.add(new DataTempatWisata(R.drawable.omahkayu, "omah kayu", "jl.candi Vi A nomer 60"));
        //3. create an adapter
        RvWisataAdapter adapter = new RvWisataAdapter(getContext(), mList,this);
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
    public void onImageCLick(DataTempatWisata wisata, ImageView imageView) {
        //disini mengirim informasi ke detailfragment
        //juga membuat trasisi animation
        Intent intent = new Intent(getContext(), WisataDetailActivity.class);
        intent.putExtra("nama tempat", wisata.getNamaTempat());
        intent.putExtra("image", wisata.getBackground());
        startActivity(intent);



        Toast.makeText(getContext(), "item clicked : "+ wisata.getNamaTempat(),Toast.LENGTH_LONG).show();

    }
}
