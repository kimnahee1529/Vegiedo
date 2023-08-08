package com.devinsight.vegiedo.view.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devinsight.vegiedo.R;

import java.util.ArrayList;

public class StoreDetailPageAdapter extends RecyclerView.Adapter<StoreDetailPageAdapter.CustomViewHolder> {

    //Item들을 담을 배열 list 생성
    private ArrayList<StoreDetailData> arrayList;

    public StoreDetailPageAdapter(ArrayList<StoreDetailData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성 =>화면 출력 최초에 생성될 때 생명주기를 의미
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_detail_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    //position(위치)에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.review_photo.setImageResource(arrayList.get(position).getIv_profile());
        holder.reviewer_name.setText(arrayList.get(position).getTv_name());
        holder.review.setText(arrayList.get(position).getTv_content());

        holder.itemView.setTag(position);
        //viewHolder 클릭 시 이벤트 구현
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.reviewer_name.getText().toString();
                Toast.makeText(view.getContext(), "curName", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    //전체 아이템 갯수 리턴
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        //
        protected ImageView review_photo;
        protected TextView reviewer_name;
        protected TextView review;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.review_photo = (ImageView) itemView.findViewById(R.id.review_photo1);
            this.reviewer_name = (TextView) itemView.findViewById(R.id.reviewer_name);
            this.review = (TextView) itemView.findViewById(R.id.review);

        }
    }

}