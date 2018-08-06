package com.maricajr.mtouch.resource;

import com.maricajr.mtouch.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAssetsSource {
// Lists for all AndroidMe images
    // Broken down into heads, bodies, legs, and all images

    private static final List<Integer> heads = new ArrayList<Integer>() {{
        add(R.drawable.ic_close_black_24dp);
        add(R.drawable.ic_view_headline_black_24dp);
    }};

    private static final List<Integer> bodies = new ArrayList<Integer>() {{
        add(R.drawable.ic_settings_black_24dp);
        add(R.drawable.ic_close_black_24dp);
    }};

    private static final List<Integer> legs = new ArrayList<Integer>() {{
        add(R.drawable.ic_keyboard_arrow_left_black_24dp);
        add(R.drawable.ic_home_black_24dp);
    }};

    private static final List<Integer> extra = new ArrayList<Integer>() {{
        add(R.drawable.ic_keyboard_arrow_left_black_24dp);
        add(R.drawable.ic_home_black_24dp);
    }};

    private static final List<Integer> all = new ArrayList<Integer>() {{
        addAll(heads);
        addAll(bodies);
        addAll(legs);
        addAll(extra);
    }};


    // Getter methods that return lists of all head images, body images, and leg images

    public static List<Integer> getHeads() {
        return heads;
    }

    public static List<Integer> getBodies() {
        return bodies;
    }

    public static List<Integer> getLegs() {
        return legs;
    }

    public static List<Integer> getExtra(){
        return extra;
    }

    // Returns a list of all the images combined: heads, bodies, and legs in that order
    public static List<Integer> getAll() {
        return all;
    }

}
