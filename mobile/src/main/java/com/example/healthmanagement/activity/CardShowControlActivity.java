package com.example.healthmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.CardControlAdapter;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;

public class CardShowControlActivity extends AppCompatActivity
        implements CardControlAdapter.OnIsShowChangedListener {
    DragSortListView listView;
    private CardControlAdapter adapter;
    private ArrayList<IsCardShow> lists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (DragSortListView) findViewById(R.id.card_show_control_list_view);
        lists.add(new IsCardShow(Record.BLOOD_PRESSURE, true));
        lists.add(new IsCardShow(Record.BLOOD_SUGAR, true));
        lists.add(new IsCardShow(Record.STEP_COUNT, true));
        lists.add(new IsCardShow(Record.HEART_RATE, false));
        adapter = new CardControlAdapter(this, R.layout.card_show_control_list_item, lists);
        adapter.setOnIsShowChangeListener(this);
        listView.setAdapter(adapter);
        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);

        DragSortController controller = new DragSortController(listView);
        controller.setDragHandleId(R.id.imageView);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);

        listView.setFloatViewManager(controller);
        listView.setOnTouchListener(controller);
        listView.setDragEnabled(true);
    }

    @Override
    public void onIsShowChanged(int p, boolean isShow) {
        lists.get(p).setShow(isShow);
    }
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                IsCardShow item = ((IsCardShow) adapter.getItem(from));
                adapter.remove(from);
                adapter.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(which);
        }
    };
}
