package com.example.faceless.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faceless.R;
import com.example.faceless.widgets.KenBurnsSupportView;

public class LaunchScreen4Fragment extends Fragment {

    KenBurnsSupportView kenBurnsSupportView;
    String image1Link = "http://candysdirt.com/wp-content/uploads/2015/01/open-workspace-interior-design-1024x768.jpg";
    String image2Link = "http://www.heimdecor.co/picture-resolution/800x600-office-layout-design-ideas-with-elegant-furniture.jpg";

    public static LaunchScreen4Fragment newInstance(Bundle b) {
        LaunchScreen4Fragment frg = new LaunchScreen4Fragment();
        frg.setArguments(b);
        return frg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.launch_screen_4_fragment, container,
                false);

        kenBurnsSupportView = (KenBurnsSupportView) v.findViewById(R.id.kenburnsviewlaunchscreens);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        kenBurnsSupportView.setResourceIds(image1Link, image2Link);
    }
}
