package com.example.asus_cp.dongmanbuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus_cp.dongmanbuy.R;

/**
 * Created by asus-cp on 2016-05-19.
 */
public class ShopStreetFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_street_fragment_layout,null);
        return v;
    }
}
