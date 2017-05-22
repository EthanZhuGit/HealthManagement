package com.example.healthmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.healthmanagement.R;
import com.example.healthmanagement.activity.NewsDetailActivity;
import com.example.healthmanagement.model.Record;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    private Context mContext;
    private List<AVObject> mList;

    public MainRecyclerAdapter(List<AVObject> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder,  int position) {
        holder.txtTitle.setText((CharSequence) mList.get(position).get("title"));
        holder.txtType.setText(getChineseName(mList.get(position).getString("type")));
        holder.txtDate.setText(dateConvert(mList.get(position).getDate("updatedAt")));
        final String objectId=mList.get(position).getObjectId();
        if (mList.get(position).getAVFile("image") == null) {
            Picasso.with(mContext)
                    .load(R.drawable.medicalcare)
                    .into(holder.mPicture);
        } else {
            Picasso.with(mContext)
                    .load(mList.get(position).getAVFile("image").getUrl())
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.mPicture);
        }
        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("goodsObjectId", objectId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtType;
        private TextView txtDate;
        private CardView newsCard;
        private ImageView mPicture;

        public MainViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.news_item_title);
            txtDate = (TextView) view.findViewById(R.id.news_item_time);
            txtType = (TextView) view.findViewById(R.id.news_item_item_tag);
            newsCard=(CardView) view.findViewById(R.id.news_card);
            mPicture = (ImageView) view.findViewById(R.id.news_item_image);

        }
    }

    private String dateConvert(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date);
    }

    private String getChineseName(String s) {
        String name="";
        switch (s) {
            case Record.BLOOD_PRESSURE:
                name = "血压";
                break;
            case Record.BLOOD_SUGAR:
                name = "血糖";
                break;
            case Record.HEART_RATE:
                name = "心率";
                break;
            case Record.BLOOD_OXYGEN:
                name = "血氧";
                break;
        }
        return name;
    }
}
