package com.example.healthmanagement.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.example.healthmanagement.R;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

//    private TextView txtNewsContent;
    private WebView webNewsContent;
    private ImageView imageView;
    private String objectId;
    private TextView detailTitle;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

//        txtNewsContent = (TextView) findViewById(R.id.txt_news_content);
        webNewsContent= (WebView) findViewById(R.id.web_news_content);
        imageView = (ImageView) findViewById(R.id.news_item_image_activity);
        toolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        objectId = getIntent().getStringExtra("goodsObjectId");
        AVObject avObject = AVObject.createWithoutData("news", objectId);
        avObject.fetchInBackground("owner", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
//                txtNewsContent.setText(avObject.getString("content"));
                webNewsContent.loadData(avObject.getString("content"),"text/html;charset=UTF-8",null);
                getSupportActionBar().setTitle(avObject.getString("title"));
                if (avObject.getAVFile("image") != null) {
                    Picasso.with(NewsDetailActivity.this)
                            .load(avObject.getAVFile("image")
                                    .getUrl())
                            .into(imageView);
                }
            }
        });

    }
}
