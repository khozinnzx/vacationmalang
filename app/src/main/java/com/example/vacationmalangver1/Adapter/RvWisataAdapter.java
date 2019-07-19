package com.example.vacationmalangver1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vacationmalangver1.Fragment.ImageItemClickListener;
import com.example.vacationmalangver1.Model.DataTempatWisata;
import com.example.vacationmalangver1.R;

import java.util.List;

public class RvWisataAdapter extends RecyclerView.Adapter<RvWisataAdapter.myViewHolder> {

    Context mContext;
    List<DataTempatWisata> mData;
    ImageItemClickListener imageItemClickListener;

    public RvWisataAdapter(Context mContext, List<DataTempatWisata> mData, ImageItemClickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        imageItemClickListener = listener;
    }

    public RvWisataAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTye) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RvWisataAdapter.myViewHolder holder, int position) {
        //animation
//        holder.namaWisata.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));


        holder.fotoWisata.setImageResource(mData.get(position).getBackground());
        holder.namaWisata.setText(mData.get(position).getNamaTempat());
        holder.alamatWisata.setText(mData.get(position).getAlamatTempat());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoWisata;
        TextView namaWisata, alamatWisata;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoWisata = itemView.findViewById(R.id.card_background);
            namaWisata = itemView.findViewById(R.id.tv_nama_wisata);
            alamatWisata = itemView.findViewById(R.id.tv_alamat);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    imageItemClickListener.onImageCLick(mData.get(getAdapterPosition()),fotoWisata);
                }
            });

        }
    }
}
