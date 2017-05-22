package com.example.healthmanagement.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.healthmanagement.R;

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
                    disableEdit();
                    Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_edit);
                } else {
                    enableEdit();
                    fab.setImageResource(R.drawable.ic_confirm);
                }

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
