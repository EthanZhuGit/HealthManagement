package com.example.healthmanagement.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.healthmanagement.R;
import com.example.healthmanagement.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {


    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText sexEdit;
    private EditText heightEdit;
    private EditText weightEdit;
    private AVUser userLocal;
    private Button logOut;
    private boolean isEdit=false;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocal = AVUser.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("个人资料");
        nameEdit = (EditText) view.findViewById(R.id.name_edit);
        nameEdit.setText(userLocal.getString("name"));
        phoneEdit = (EditText) view.findViewById(R.id.phone_edit);
        phoneEdit.setText(userLocal.getMobilePhoneNumber());
        phoneEdit.setEnabled(false);
        heightEdit = (EditText) view.findViewById(R.id.height_edit);
        heightEdit.setText(userLocal.getString("height"));
        sexEdit = (EditText) view.findViewById(R.id.sex_edit);
        sexEdit.setText(userLocal.getString("sex"));
        weightEdit = (EditText) view.findViewById(R.id.weight_edit);
        weightEdit.setText(userLocal.getString("weight"));
        disableEdit();
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    userLocal.put("name", nameEdit.getText());
                    userLocal.put("sex", sexEdit.getText());
                    userLocal.put("height",Float.valueOf((heightEdit.getText().toString())));
                    userLocal.put("weight", Float.valueOf(weightEdit.getText().toString()));
                    userLocal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "同步成功", Toast.LENGTH_SHORT).show();
                                disableEdit();
                                fab.setImageResource(R.drawable.ic_edit);
                            }
                        }
                    });
                } else {
                    enableEdit();
                    fab.setImageResource(R.drawable.ic_confirm);
                }

            }
        });

        logOut = (Button) view.findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.getCurrentUser().logOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

    private void disableEdit() {
        nameEdit.setEnabled(false);
//        phoneEdit.setEnabled(false);
        heightEdit.setEnabled(false);
        sexEdit.setEnabled(false);
        weightEdit.setEnabled(false);
        isEdit = false;
    }

    private void enableEdit() {
        nameEdit.setEnabled(true);
//        phoneEdit.setEnabled(true);
        heightEdit.setEnabled(true);
        sexEdit.setEnabled(true);
        weightEdit.setEnabled(true);
        isEdit = true;
    }

}
