package com.example.android.sensed.ui.fragment.main_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sensed.R;
import com.example.android.sensed.ui.fragment.BaseFragment;

/**
 * Created by mnt_x on 28/05/2017.
 */

public class ChatFragment extends BaseFragment {

    public static ChatFragment create(){

        return new ChatFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
