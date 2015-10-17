package com.example.faceless.activities;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.faceless.R;
import com.example.faceless.fragments.LaunchScreen1Fragment;
import com.example.faceless.fragments.LaunchScreen4Fragment;
import com.example.faceless.widgets.CirclePageIndicator;
import com.example.faceless.widgets.ParallaxViewPagerTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class LaunchActivity extends BaseActivity implements OnClickListener, ViewPager.OnPageChangeListener {

    Button joinTeam, createTeam;
    ViewPager viewPager;
    ArgbEvaluator argbEvaluator;
    FrameLayout mainContainerLayout;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    MyPagerAdapter adapter;
    ImageView launchIcon;
    int deviceHeight;
    CirclePageIndicator pageIndicator;
    LinearLayout loginButtonsContainerLayout, loginButtonsLayout;
    int loginButtonsLayoutHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity_layout);

        joinTeam = (Button) findViewById(R.id.join);
        createTeam = (Button) findViewById(R.id.creeate);
        viewPager = (ViewPager) findViewById(R.id.pager_launch);
        mainContainerLayout = (FrameLayout) findViewById(R.id.launch_activity_main);
        launchIcon = (ImageView) findViewById(R.id.ic_app_icon_launch);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);
        loginButtonsContainerLayout = (LinearLayout) findViewById(R.id.indicatorandbuttonslayout);
        loginButtonsLayout = (LinearLayout) findViewById(R.id.login_buttons);

        joinTeam.setOnClickListener(this);
        createTeam.setOnClickListener(this);

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Scroller scroller = new Scroller(this,
                    new DecelerateInterpolator(), true);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
        }

        viewPager.setPageTransformer(true, new ParallaxViewPagerTransformer(
                R.id.imagelaunch));

        loginButtonsLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mainContainerLayout.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        } else {
                            mainContainerLayout.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                        loginButtonsLayoutHeight = loginButtonsLayout
                                .getHeight();
                    }
                });

        mainContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mainContainerLayout.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        } else {
                            mainContainerLayout.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                        deviceHeight = mainContainerLayout.getHeight();
                    }
                });

        argbEvaluator = new ArgbEvaluator();
        colors.add(getResources().getColor(R.color.home_viewpager_color_4));
        colors.add(getResources().getColor(R.color.home_viewpager_color_2));
        colors.add(getResources().getColor(R.color.home_viewpager_color_3));
        colors.add(getResources().getColor(R.color.home_viewpager_color_1));

        pageIndicator.setOnPageChangeListener(this);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.creeate:
                i = new Intent(this, SignUpActivity.class);
                startActivity(i);

            default:
                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", pos);
            switch (pos) {
                case 3:
                    return LaunchScreen4Fragment.newInstance(bundle);

                default:
                    return LaunchScreen1Fragment.newInstance(bundle);
            }
        }

        @Override
        public int getCount() {
            return colors.size();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (position < (adapter.getCount() - 1)
                && position < (colors.size() - 1)) {
            mainContainerLayout.setBackgroundColor((Integer) argbEvaluator
                    .evaluate(positionOffset, colors.get(position),
                            colors.get(position + 1)));
        } else {
            mainContainerLayout
                    .setBackgroundColor(colors.get(colors.size() - 1));
        }
        if ((viewPager.getCurrentItem() == 0 && position == 0)
                || (viewPager.getCurrentItem() == 1 && position == 0)) {
            translateLauncherIconUp(positionOffset);
            scaleLauncherIcon(positionOffset);
            fadeLauncherIcon(positionOffset);
            translateLoginButtons(positionOffset);
        } else if ((viewPager.getCurrentItem() == 2 && position == 2)
                || (viewPager.getCurrentItem() == 3 && position == 2 && positionOffset != 0)) {
            translateLauncherIconUp(1 - positionOffset);
            fadeLauncherIcon(1 - positionOffset);
            scaleLauncherIcon(1 - positionOffset);
            translateLoginButtons(1 - positionOffset);
        }
    }

    private void translateLoginButtons(float positionOffset) {
        float trans = positionOffset * loginButtonsLayoutHeight;
        loginButtonsContainerLayout.setTranslationY(trans);
    }

    private void scaleLauncherIcon(float positionOffset) {
        if (positionOffset <= 0.5) {
            launchIcon.setScaleX(1 - positionOffset);
            launchIcon.setScaleY(1 - positionOffset);
        } else {
            launchIcon.setScaleX(0.5f);
            launchIcon.setScaleY(0.5f);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void fadeLauncherIcon(float positionOffset) {
        if (positionOffset >= 0.5) {
            float fade = (float) ((((positionOffset - 0.5) * (0 - 255)) / (1 - 0.5)) + 255);
            launchIcon.setImageAlpha((int) fade);
        } else {
            launchIcon.setImageAlpha(255);
        }
    }

    private void translateLauncherIconUp(float positionOffset) {
        if (positionOffset >= 0.5)
            positionOffset = 0.5f;
        float trans = (deviceHeight - getResources().getDimensionPixelSize(
                R.dimen.bnc_launch_app_icon_margin))
                * positionOffset * -1;
        launchIcon.setTranslationY(trans);
    }

    @Override
    public void onPageSelected(int pos) {

    }
}
