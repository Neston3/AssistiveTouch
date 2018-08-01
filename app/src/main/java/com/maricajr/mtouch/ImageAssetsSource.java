package com.maricajr.mtouch;

import java.util.ArrayList;
import java.util.List;

public class ImageAssetsSource {

    private static final List<Integer> imageAsset=new ArrayList<Integer>(){{
        add(R.mipmap.float_close);
        add(R.mipmap.float_home);
        add(R.mipmap.float_back);
        add(R.mipmap.float_recent);
        add(R.mipmap.float_setting);
    }};

    private static final List<Integer> all=new ArrayList<Integer>(){{
        addAll(imageAsset);
    }};

    public static List<Integer> getImageAsset(){
       return imageAsset;
    }

    public static List<Integer> getAll(){
        return all;
    }

}
