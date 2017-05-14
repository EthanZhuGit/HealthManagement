package com.example.healthmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.model.IsCardShow;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/14 0014.
 */

public class CardControlAdapter extends BaseAdapter {
    private Context context;
    private List<IsCardShow> items;
    private int resourceId;
    private OnIsShowChangedListener onIsShowChangedListener;
    public void setOnIsShowChangeListener(OnIsShowChangedListener onIsShowChangeListener){
        this.onIsShowChangedListener=onIsShowChangeListener;
    }

    public CardControlAdapter(Context context,int resourceId, List<IsCardShow> list) {
        this.context=context;
        this.items=list;
        this.resourceId=resourceId;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void remove(int arg0) {//删除指定位置的item
        items.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(IsCardShow item, int arg0) {
        items.add(arg0, item);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final IsCardShow isCardShow=(IsCardShow) getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
        } else {
            view=convertView;
        }
        TextView name = (TextView) view.findViewById(R.id.textView1);
        name.setText(isCardShow.getName());
        final Switch s = (Switch) view.findViewById(R.id.switcher);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onIsShowChangedListener.onIsShowChanged(position, isChecked);
                isCardShow.setShow(isChecked);
            }
        });
        if (s.isChecked() !=isCardShow.isShow()) {
            s.setChecked(isCardShow.isShow());

        }

        return view;
    }

    public interface OnIsShowChangedListener{
        public void onIsShowChanged(int p,boolean isShow);
    }
}
