package com.example.asus_cp.dongmanbuy.activity.product_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.fragment.HomeFragment;
import com.example.asus_cp.dongmanbuy.fragment.comments.AllCommentFragment;
import com.example.asus_cp.dongmanbuy.fragment.comments.ChaCommentFragment;
import com.example.asus_cp.dongmanbuy.fragment.comments.HaoCommentFragment;
import com.example.asus_cp.dongmanbuy.fragment.comments.YouTuCommentFragment;
import com.example.asus_cp.dongmanbuy.fragment.comments.ZhongCommentFragment;
import com.example.asus_cp.dongmanbuy.model.Good;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评论的界面
 * Created by asus-cp on 2016-06-02.
 */
public class CommetnActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout allCommentLinearLayout;//全部评价
    private LinearLayout haoCommentLinearLayout;//好评
    private LinearLayout zhongCommentLinearLayout;//中评
    private LinearLayout chaCommentLinearLayout;//好评
    private LinearLayout youTuCommentLinearLayout;//有图评价

    private TextView allCommentTextView;//全部评价这几个字
    private TextView haoCommentTextView;//好评这几个字
    private TextView zhongCommentTextView;//中评这几个字
    private TextView chaCommentTextView;//差评这几个字
    private TextView youTuCommentTextView;//有图评价这几个字

    private TextView allCommentCountTextView;//全部评价数量
    private TextView haoCommentCountTextView;//好评数量
    private TextView zhongCommentCountTextView;//中评数量
    private TextView chaCommentCountTextView;//差评数量
    private TextView youTuCommentCountTextView;//有图评价数量

    public static final int ALL_COMMENT=0;
    public static final int HAO_COMMENT=1;
    public static final int ZHONG_COMMENT=2;
    public static final int CHA_COMMENT=3;
    public static final int YOU_TU_COMMENT=4;

    private ViewPager viewPager;

    private List<Fragment> fragments;

    private String userCommentUrl = "http://www.zmobuy.com/PHP/index.php?url=/comments";//用户评论的接口

    private String tag="CommetnActivity";

    private Good good;

    private String youTu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.comment_actvity_layout);
        setTitle(R.string.comment);
        initView();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setIsNeedTongJiPage(false);
    }

    @Override
    public void initView() {
        allCommentLinearLayout= (LinearLayout) findViewById(R.id.ll_all_comment);
        haoCommentLinearLayout= (LinearLayout) findViewById(R.id.ll_hao_comment);
        zhongCommentLinearLayout= (LinearLayout) findViewById(R.id.ll_zhong_comment);
        chaCommentLinearLayout= (LinearLayout) findViewById(R.id.ll_cha_comment);
        youTuCommentLinearLayout= (LinearLayout) findViewById(R.id.ll_you_tu_comment);

        allCommentLinearLayout.setOnClickListener(this);
        haoCommentLinearLayout.setOnClickListener(this);
        zhongCommentLinearLayout.setOnClickListener(this);
        chaCommentLinearLayout.setOnClickListener(this);
        youTuCommentLinearLayout.setOnClickListener(this);

        allCommentTextView= (TextView) findViewById(R.id.text_all_comment);
        haoCommentTextView= (TextView) findViewById(R.id.text_hao_comment);
        zhongCommentTextView= (TextView) findViewById(R.id.text_zhong_comment);
        chaCommentTextView= (TextView) findViewById(R.id.text_cha_comment);
        youTuCommentTextView= (TextView) findViewById(R.id.text_you_tu_comment);

        allCommentCountTextView= (TextView) findViewById(R.id.text_all_comment_count);
        haoCommentCountTextView= (TextView) findViewById(R.id.text_hao_comment_count);
        zhongCommentCountTextView= (TextView) findViewById(R.id.text_zhong_comment_count);
        chaCommentCountTextView= (TextView) findViewById(R.id.text_cha_comment_count);
        youTuCommentCountTextView= (TextView) findViewById(R.id.text_you_tu_comment_count);
    }

    /**
     * 初始化的方法
     */
    private void init() {
        good=getIntent().getParcelableExtra(HomeFragment.GOOD_KEY);
        youTu=getIntent().getStringExtra(MyConstant.YOU_TU_PING_JIA_KEY);

        //设置各种评论的数量
        StringRequest commentRequst=new StringRequest(Request.Method.POST, userCommentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "返回的数据是" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject countJsonobj = jsonObject.getJSONObject("paginated");
                    String count = countJsonobj.getString("total");
                    allCommentCountTextView.setText(count);
                    haoCommentCountTextView.setText(count);
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("json", "{\"goods_id\":\"" + good.getGoodId() + "\",\"pagination\":{\"page\":\"1\",\"count\":\"1\"}}");
                return map;
            }
        };
        requestQueue.add(commentRequst);

        allCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        allCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));

        viewPager= (ViewPager) findViewById(R.id.view_pager_comment);

        fragments=new ArrayList<Fragment>();
        AllCommentFragment allCommentFragment=new AllCommentFragment();
        HaoCommentFragment haoCommentFragment=new HaoCommentFragment();
        ZhongCommentFragment zhongCommentFragment=new ZhongCommentFragment();
        ChaCommentFragment chaCommentFragment=new ChaCommentFragment();
        YouTuCommentFragment youTuCommentFragment=new YouTuCommentFragment();
        fragments.add(allCommentFragment);
        fragments.add(haoCommentFragment);
        fragments.add(zhongCommentFragment);
        fragments.add(chaCommentFragment);
        fragments.add(youTuCommentFragment);
        FragmentManager fragmentManager=getSupportFragmentManager();
        MyFragmentPagerAdapter myFragmentPagerAdapter=new MyFragmentPagerAdapter(fragmentManager);
        viewPager.setAdapter(myFragmentPagerAdapter);

        //有图评价跳转过来
        if(MyConstant.YOU_TU_PING_JIA_CONTENT.equals(youTu)){
            reset();
            viewPager.setCurrentItem(YOU_TU_COMMENT);
            youTuCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
            youTuCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                reset();
                switch (position){
                    case ALL_COMMENT:
                        allCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        allCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case HAO_COMMENT:
                        haoCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        haoCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case ZHONG_COMMENT:
                        zhongCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        zhongCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case CHA_COMMENT:
                        chaCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        chaCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                    case YOU_TU_COMMENT:
                        youTuCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        youTuCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        reset();
        switch (v.getId()){
            case R.id.ll_all_comment:
                allCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                allCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(ALL_COMMENT);
                break;
            case R.id.ll_hao_comment:
                haoCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                haoCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(HAO_COMMENT);
                break;
            case R.id.ll_zhong_comment:
                zhongCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                zhongCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(ZHONG_COMMENT);
                break;
            case R.id.ll_cha_comment:
                chaCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                chaCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(CHA_COMMENT);
                break;
            case R.id.ll_you_tu_comment:
                youTuCommentTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                youTuCommentCountTextView.setTextColor(getResources().getColor(R.color.bottom_lable_color));
                viewPager.setCurrentItem(YOU_TU_COMMENT);
                break;
        }
    }


    /**
     * 返回good的方法
     */
    public Good getGood(){
        return good;
    }

    /**
     * 将标题的颜色重置为黑色
     */
    public void reset(){
        allCommentTextView.setTextColor(getResources().getColor(R.color.myblack));
        haoCommentTextView.setTextColor(getResources().getColor(R.color.myblack));
        zhongCommentTextView.setTextColor(getResources().getColor(R.color.myblack));
        chaCommentTextView.setTextColor(getResources().getColor(R.color.myblack));
        youTuCommentTextView.setTextColor(getResources().getColor(R.color.myblack));

        allCommentCountTextView.setTextColor(getResources().getColor(R.color.myblack));
        haoCommentCountTextView.setTextColor(getResources().getColor(R.color.myblack));
        zhongCommentCountTextView.setTextColor(getResources().getColor(R.color.myblack));
        chaCommentCountTextView.setTextColor(getResources().getColor(R.color.myblack));
        youTuCommentCountTextView.setTextColor(getResources().getColor(R.color.myblack));
    }

    /**
     * 我自己viewpager的适配器
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
