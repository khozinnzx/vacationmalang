package com.example.vacationmalangver1.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vacationmalangver1.Model.DataTempatWisata;
import com.example.vacationmalangver1.R;

import java.util.List;

public class RvWisataAdapter extends RecyclerView.Adapter<RvWisataAdapter.myViewHolder> {

    List<DataTempatWisata> mData;
    OnClickRvListener mOnClickRvListener;

    public RvWisataAdapter(List<DataTempatWisata> mData, OnClickRvListener mOnClickRvListener) {
        this.mData = mData;
        this.mOnClickRvListener = mOnClickRvListener;
    }

    public RvWisataAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTye) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new myViewHolder(v,mOnClickRvListener);

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

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView fotoWisata;
        TextView namaWisata, alamatWisata;
        Button btnDetail;
        OnClickRvListener onClickRvListener;

        public myViewHolder(@NonNull View itemView, OnClickRvListener onClickRvListener) {
            super(itemView);

            fotoWisata = itemView.findViewById(R.id.card_background);
            namaWisata = itemView.findViewById(R.id.tv_nama_wisata);
            alamatWisata = itemView.findViewById(R.id.tv_alamat);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            this.onClickRvListener = onClickRvListener;
            btnDetail.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickRvListener.onClickRv(getAdapterPosition());
        }
    }

    public interface OnClickRvListener {
        void onClickRv(int position);
    }
}
