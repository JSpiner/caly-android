package io.caly.calyandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.caly.calyandroid.CalyApplication;
import io.caly.calyandroid.fragment.RecoTabFragment;
import io.caly.calyandroid.model.Category;
import io.caly.calyandroid.model.dataModel.EventModel;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project CalyAndroid
 * @since 17. 2. 28
 */

public class RecoTabPagerAdapter extends FragmentStatePagerAdapter {

    //로그에 쓰일 tag
    private static final String TAG = CalyApplication.class.getSimpleName() + "/" + RecoTabPagerAdapter.class.getSimpleName();

    Fragment[] fragmentList;

    public RecoTabPagerAdapter(FragmentManager fm, EventModel eventData) {
        super(fm);

        fragmentList= new Fragment[]{
                new RecoTabFragment().setCategory(Category.RESTAURANT).setEvent(eventData),
                new RecoTabFragment().setCategory(Category.CAFE).setEvent(eventData),
                new RecoTabFragment().setCategory(Category.PLACE).setEvent(eventData)
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        return fragmentList.length;
    }
}
