package com.example.vacationmalangver1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vacationmalangver1.Model.UrutanWisata;
import com.example.vacationmalangver1.R;

import java.util.ArrayList;
import java.util.List;

public class MapRvAdapter extends RecyclerView.Adapter<MapRvAdapter.myViewHolder> {

    private ArrayList<UrutanWisata> mWisata = new ArrayList<>();
    private MapRvClickListener mClickListener;


    public MapRvAdapter(ArrayList<UrutanWisata> mWisata, MapRvClickListener mClickListener) {
        this.mWisata = mWisata;
        this.mClickListener = mClickListener;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_map_wisata_list, parent, false);
        final myViewHolder holder = new myViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        String p = String.valueOf(position+1);
        holder.namaWisata.setText(mWisata.get(position).getNamaWisata());
        holder.urutan.setText("urutan "+p);


    }

    @Override
    public int getItemCount() {
        return mWisata.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namaWisata, urutan;
        MapRvClickListener mClickListener;

        public myViewHolder(View itemView, MapRvClickListener clickListener) {
            super(itemView);
            namaWisata = itemView.findViewById(R.id.tv_urutanwisata);
            urutan = itemView.findViewById(R.id.tv_index_urutan);

            mClickListener = clickListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            mClickListener.onMapRvClicked(getAdapterPosition());

        }
    }

    public interface MapRvClickListener {
        void onMapRvClicked(int position);
    }
}
