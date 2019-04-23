package com.gearback.zt.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gearback.methods.Methods;
import com.gearback.zt.login.R;

public class ImageTypeDialog extends BottomSheetDialogFragment {

    Methods methods = new Methods();
    TextView cameraBtn, galleryBtn;

    int action = 0;

    public void sendResults(int RequestCode) {
        Intent intent = new Intent();
        intent.putExtra("action", action);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RequestCode, intent);
    }
    public static ImageTypeDialog newInstance() {
        ImageTypeDialog f = new ImageTypeDialog();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.field_image_type, container, false);
        cameraBtn = view.findViewById(R.id.fieldCameraBtn);
        galleryBtn = view.findViewById(R.id.fieldGalleryBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = 1;
                sendResults(1001);
                dismiss();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = 2;
                sendResults(1001);
                dismiss();
            }
        });

        return view;
    }
}
