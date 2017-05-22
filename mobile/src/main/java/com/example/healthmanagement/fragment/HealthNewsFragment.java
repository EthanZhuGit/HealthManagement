package com.example.healthmanagement.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.R;
import com.example.healthmanagement.adapter.MainRecyclerAdapter;
import com.example.healthmanagement.model.IsCardShow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthNewsFragment extends Fragment {
    private static final String TAG = "TAG" + "HealthNewsFragment";

    public HealthNewsFragment() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mRecyclerAdapter;
    private List<AVObject> mList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_news, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new MainRecyclerAdapter(mList, getContext());
        mRecyclerView.setAdapter(mRecyclerAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");
                mList.clear();
                downLoadNews();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        downLoadNews();
        return view;
    }

    private void downLoadNews() {
        List<IsCardShow> cardShowList = HelpUtils.getCardShowControlList(getContext());
        for (IsCardShow i : cardShowList) {
            if (i.isShow()) {
                AVQuery<AVObject> queryNews = new AVQuery<>("news");
                queryNews.orderByDescending("createAt");
                queryNews.selectKeys(Arrays.asList("title", "type", "image"));
                queryNews.whereEqualTo("type", i.getName());
                Log.d(TAG, "downLoadNews: " + i.getName());
                queryNews.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            mList.addAll(list);
                            Log.d(TAG, "done: " + mList.size());
                            mRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }



}
