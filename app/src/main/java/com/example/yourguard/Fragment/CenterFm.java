package com.example.yourguard.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourguard.AccountActivity;
import com.example.yourguard.MainActivity;
import com.example.yourguard.R;
import com.example.yourguard.SingleActivity;

public class CenterFm extends Fragment {
    private View mview;
    private Boolean isFirst = true;
    private TextView device_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fm_center, container, false);

        device_name = mview.findViewById(R.id.device_name);
        LinearLayout device = mview.findViewById(R.id.device_lo);
        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        LinearLayout account_setting = mview.findViewById(R.id.setting_lo);
        account_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return mview;
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog;
        int id = 0;
        if (! isFirst) id = 1;
        builder.setSingleChoiceItems(new String[]{"妈妈的血压计", "爸爸的血压计"}, id, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        isFirst = true;
                        device_name.setText("妈妈的血压计");
                        dialog.dismiss();
                        break;
                    case 1:
                        isFirst = false;
                        device_name.setText( "爸爸的血压计");
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_view);
        dialog.show();
    }
}
