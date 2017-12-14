package com.pgg.mywechatem.Fragment;

import java.util.HashMap;

/**
 * Created by PDD on 2017/11/16.
 */

public class FragmentFactory {

    private static HashMap<Integer,BaseFragment> hashMapFragment =new HashMap<Integer,BaseFragment>();
    public static BaseFragment createFragment(int pos){
        BaseFragment baseFragment= hashMapFragment.get(pos);
        if (baseFragment==null){
            switch (pos){
                case 0:
                    baseFragment=new MessageFragment();
                    break;
                case 1:
                    baseFragment=new ContactFragment();
                    break;
                case 2:
                    baseFragment=new FindFragment();
                    break;
                case 3:
                    baseFragment=new ProfileFragment();
                    break;
                default:
                    break;
            }
        }
        hashMapFragment.put(pos,baseFragment);
        return baseFragment;
    }
}
