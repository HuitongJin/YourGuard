package com.example.yourguard.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourguard.DynamicActivity;
import com.example.yourguard.MainActivity;
import com.example.yourguard.R;
import com.example.yourguard.SingleActivity;

public class MeasureFm extends Fragment {

    private View mview;
    private ImageView Single;   // 单次测量按钮
    private ImageView Dynamic;  // 动态测量按钮

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fm_measure, container, false);

        // 初始化控件
        Single = mview.findViewById(R.id.single_btn);
        Dynamic = mview.findViewById(R.id.dynamic_btn);

        // 单次测量触发事件
        Single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SingleActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // 动态测量触发事件
        Dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DynamicActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return mview;
    }
}
