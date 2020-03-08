package com.fmbg.moobuni;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdappterUNI extends FragmentPagerAdapter {
    public TabsAdappterUNI(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                chatFragment chatfragment = new chatFragment();
                return chatfragment;
            case 1:
                UNInewsFragment uninewsfragment = new UNInewsFragment();
                return uninewsfragment;
            case 2:
                Uni_AKIS_Fragment uni_akis_Fragment = new Uni_AKIS_Fragment();
                return uni_akis_Fragment;
            default:
                return null;

        }}

    @Override
    public int getCount () {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle ( int position){
        switch (position) {
            case 0:
                return "SORULAR";
            case 1:
                return "HABERLER";
            case 2:
                return "ETKİNLİKLER";
            default:
                return null;

        }
    }
}