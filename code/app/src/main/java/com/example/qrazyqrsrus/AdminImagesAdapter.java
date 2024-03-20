package com.example.qrazyqrsrus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminImagesAdapter extends ArrayAdapter<RetrievedImage> {
    public AdminImagesAdapter(Context context, ArrayList<RetrievedImage> images) {
        super(context, 0, images);
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view;
//        if (convertView == null) {
//            //view = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
//        } else {
//            view = convertView;
//        }
//
//        return view;
//    }
}
