package com.example.healthmanagement.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.CardControlAdapter;
import com.example.healthmanagement.fragment.HomeFragment;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

public class CardShowControlActivity extends AppCompatActivity
        implements CardControlAdapter.OnIsShowChangedListener {
    private static final String TAG = "TAG" + "CardShowControlAct";

    DragSortListView listView;
    private CardControlAdapter adapter;
    private ArrayList<IsCardShow> lists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_show_control);
        listView = (DragSortListView) findViewById(R.id.card_show_control_list_view);
        Bundle bundle = getIntent().getExtras();
        lists = bundle.getParcelableArrayList("list_send");
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


    @Override
    public void onBackPressed() {
        ArrayList<IsCardShow> listReturn = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            listReturn.add((IsCardShow) adapter.getItem(i));
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list_return", listReturn);
        intent.putExtras(bundle);
        setResult(HomeFragment.RESULT_CODE_CARDCONTROL, intent);
        Log.d(TAG, "BackPressed");
        super.onBackPressed();
    }
}
