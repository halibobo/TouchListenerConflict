package me.daei.touchlistenerconflict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.id_viewpager)
    ViewPager idViewpager;

    @OnClick({R.id.id_indicator_one,R.id.id_indicator_two,R.id.id_indicator_three,R.id.id_indicator_four})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.id_indicator_one:
                clickTab(0);
            break;
            case R.id.id_indicator_two:
                clickTab(1);
                break;
            case R.id.id_indicator_three:
                clickTab(2);
                break;
            case R.id.id_indicator_four:
                clickTab(3);
                break;
        }
    }

    private ViewPagerAdapter mPagerAdapter;
    private final int PAGE_SIZE = 4;
    private Fragment[] rootFragments = new Fragment[PAGE_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (idViewpager != null) {
            setupViewPager(idViewpager);
        }
        clickTab(0);
    }

    private void clickTab(int tabType) {
        idViewpager.setCurrentItem(tabType, true);
        changeTab(tabType);
    }

    public void setViewpagerNoSCroll(boolean scroll) {
        if(!scroll){
            idViewpager.requestDisallowInterceptTouchEvent(true);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < PAGE_SIZE; i++) {
            switch (i) {
                case 0:
                    rootFragments[i] = new MainFragment();
                    break;
                case 1:
                    rootFragments[i] = new AboutFragment();
                    break;
                case 2:
                    rootFragments[i] = new AboutFragment();
                    break;
                case 3:
                    rootFragments[i] = new AboutFragment();
                    break;
            }
            Log.e("rootFragments", i + "= " + rootFragments[i]);
            rootFragments[i].setRetainInstance(true);
            mPagerAdapter.addFragment(rootFragments[i], getFragmentTag(i));
        }
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(PAGE_SIZE);
    }

    private void changeTab(int tabType) {
    }

    private String getFragmentTag(int pos) {
        String tag = "pos_default";
        switch (pos) {
            case 0:
                tag = "功能1";
                break;
            case 1:
                tag =  "功能2";
                break;
            case 2:
                tag =  "功能3";
                break;
            case 3:
                tag =  "功能4";
                break;
        }
        return tag;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
