package com.fmbg.moobuni;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdappterSCH extends FragmentPagerAdapter {
    public TabsAdappterSCH(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HighSchoolQuestionFragment schschoolfragment =new HighSchoolQuestionFragment();
                return schschoolfragment;
            case 1:
                chatHighSchoolFragment chatfragment =new chatHighSchoolFragment();
                return chatfragment;
            default :
                return null;

        }
        /*if(position ==0){
        HighSchoolQuestionFragment  schschoolfragment =new HighSchoolQuestionFragment();
        return schschoolfragment;
        }
        else if(position==1){
            chatFragment chatfragment =new chatFragment();
            return chatfragment;
        }
        else
        return null;*/
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "SORULAR";
            case 1:
                return "MESAJLAR";
            default :
                    return null;

        }
        /*if(position==0){
            return "Okullar";
        }
        else if (position==1){
            return "Mesajlar";
        }
        else
            return null;*/
    }
}
