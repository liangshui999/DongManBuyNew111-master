package com.example.asus_cp.dongmanbuy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.MainActivity;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.CategoryListFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.DIYFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.MaoRongFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.MoXingFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.PeiShiFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.ShangZhuangFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.ShuJiFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.XiaZhuangFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.XiangBaoFragment;
import com.example.asus_cp.dongmanbuy.fragment.category_item_fragments.ZaiPingFragment;
import com.example.asus_cp.dongmanbuy.util.MyLog;

/**
 * 分类子标签
 * Created by asus-cp on 2016-05-19.
 */
public class CategoryFragment extends Fragment {

    private String tag="CategoryFragment";

    private FrameLayout cateGoryListFrameLayout;
    private FrameLayout contentFrameLayout;
    private ListView categoryList;


    private FragmentManager manager;

    private CategoryListFragment categoryListFragment;
    private XiangBaoFragment xiangBaoFragment;
    private ShangZhuangFragment shangZhuangFragment;
    private XiaZhuangFragment xiaZhuangFragment;
    private MaoRongFragment maoRongFragment;
    private PeiShiFragment peiShiFragment;
    private ShuJiFragment shuJiFragment;
    private DIYFragment diyFragment;
    private ZaiPingFragment zaiPingFragment;
    private MoXingFragment moXingFragment;

    private static final int SHANG_ZHUANG=0;
    private static final int XIA_ZHUANG=1;
    private static final int XIANG_BAO=2;
    private static final int ZAI_PIN=3;
    private static final int MAO_RONG=4;
    private static final int PEI_SHI=5;
    private static final int SHU_JI=6;
    private static final int DIY_DING_ZHI=7;
    private static final int MO_XING=8;

    private int density;//屏幕像素密度

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.category_fragment_layout,null);
//        cateGoryListFrameLayout= (FrameLayout) v.findViewById(R.id.frame_layout_category_list);
//        contentFrameLayout= (FrameLayout) v.findViewById(R.id.frame_layout_buf);
        categoryListFragment=new CategoryListFragment();
        xiangBaoFragment=new XiangBaoFragment();
        shangZhuangFragment=new ShangZhuangFragment();
        xiaZhuangFragment=new XiaZhuangFragment();
        maoRongFragment=new MaoRongFragment();
        peiShiFragment=new PeiShiFragment();
        shuJiFragment=new ShuJiFragment();
        diyFragment=new DIYFragment();
        zaiPingFragment=new ZaiPingFragment();
        moXingFragment=new MoXingFragment();

        manager=getChildFragmentManager();//注意用的是这个方法
        final FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.frame_layout_category_list, categoryListFragment);
        transaction.add(R.id.frame_layout_buf, shangZhuangFragment);
        transaction.commit();

        MainActivity mainActivity= (MainActivity) getActivity();
        density=mainActivity.getXiangSuMiDu();
        MyLog.d(tag,"密度"+density);

        return v;
    }

    public int getDensity(){
        return density;
    }


    @Override
    public void onResume() {
        super.onResume();
        categoryList=categoryListFragment.getListView();
        //categoryList.getAdapter().getView(0,null,categoryList).setBackgroundResource(R.color.white_my);



        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private View lastView;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case SHANG_ZHUANG:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        FragmentTransaction transaction1 = manager.beginTransaction();
                        transaction1.replace(R.id.frame_layout_buf, shangZhuangFragment);
                        transaction1.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case XIA_ZHUANG:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                            MyLog.d(tag,"执行了吗");
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction2 = manager.beginTransaction();
                        transaction2.replace(R.id.frame_layout_buf, xiaZhuangFragment);
                        transaction2.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case XIANG_BAO:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction3 = manager.beginTransaction();
                        transaction3.replace(R.id.frame_layout_buf, xiangBaoFragment);
                        transaction3.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case ZAI_PIN:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction4 = manager.beginTransaction();
                        transaction4.replace(R.id.frame_layout_buf, zaiPingFragment);
                        transaction4.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case MAO_RONG:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction5 = manager.beginTransaction();
                        transaction5.replace(R.id.frame_layout_buf, maoRongFragment);
                        transaction5.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case PEI_SHI:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction6 = manager.beginTransaction();
                        transaction6.replace(R.id.frame_layout_buf, peiShiFragment);
                        transaction6.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case SHU_JI:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction7 = manager.beginTransaction();
                        transaction7.replace(R.id.frame_layout_buf, shuJiFragment);
                        transaction7.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case DIY_DING_ZHI:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction8 = manager.beginTransaction();
                        transaction8.replace(R.id.frame_layout_buf, diyFragment);
                        transaction8.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                    case MO_XING:
                        if(lastView!=null){
                            lastView.setBackgroundResource(R.color.gray);
                        }
                        getViewByPosition(0, (ListView) parent).setBackgroundResource(R.color.gray);
                        FragmentTransaction transaction9 = manager.beginTransaction();
                        transaction9.replace(R.id.frame_layout_buf, moXingFragment);
                        transaction9.commit();
                        view.setBackgroundResource(R.color.white_my);
                        lastView=view;
                        break;
                }
            }
        });
    }

    /**
     * 获取listview中的某一项的view
     *
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
