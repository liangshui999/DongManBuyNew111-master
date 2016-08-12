package com.example.asus_cp.dongmanbuy.activity.map_activity_my;

import android.app.Activity;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 线路规划的界面
 * Created by asus-cp on 2016-07-27.
 */
public class SearchWayActivity extends Activity{

   /* private String tag="SearchWayActivity";

    private LinearLayout gongJiaoLinearLayout;//公交
    private LinearLayout jiaCheLinearLayout;//驾车
    private LinearLayout buXingLinearLayout;//步行
    private TextView gongJiaoTextView;
    private TextView jiaCheTextView;
    private TextView buXingTextView;
    private EditText myLocationEditText;//我的位置
    private EditText dianPuLocationEditText;//店铺位置
    private Button searchButton;//查询按钮
    private ListView gongJiaoResultListView;//公交搜索结果展示
    private ListView jiaCheResultListView;//公交搜索结果展示

    private LatLng myPosition;//我的位置
    private LatLng dianPuPosition;//店铺的位置
    private ShopModel shopModel;//店铺实体


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_way_activity_layout);
        init();
    }


    *//**
     * 初始化的方法
     *//*
    private void init() {
        gongJiaoLinearLayout= (LinearLayout) findViewById(R.id.ll_gong_jiao_search_way);
        jiaCheLinearLayout= (LinearLayout) findViewById(R.id.ll_jia_che_search_way);
        buXingLinearLayout= (LinearLayout) findViewById(R.id.ll_bu_xing_search_way);
        gongJiaoTextView= (TextView) findViewById(R.id.text_gong_jiao_search_way);
        jiaCheTextView= (TextView) findViewById(R.id.text_jia_che_search_way);
        buXingTextView= (TextView) findViewById(R.id.text_bu_xing_search_way);
        dianPuLocationEditText= (EditText) findViewById(R.id.edit_dao_search_way);
        myLocationEditText= (EditText) findViewById(R.id.edit_cong_search_way);
        searchButton= (Button) findViewById(R.id.btn_search_search_way);
        gongJiaoResultListView = (ListView) findViewById(R.id.list_gong_jiao_search_result_search_way);
        jiaCheResultListView= (ListView) findViewById(R.id.list_jia_che_search_result_search_way);

        //获取到我的位置和店铺的位置
        myPosition=getIntent().getParcelableExtra(MyConstant.MY_LOCATION_KEY);
        dianPuPosition=getIntent().getParcelableExtra(MyConstant.DIAN_PU_LOCATION_KEY);
        shopModel=getIntent().getParcelableExtra(MyConstant.SHOP_MODEL_KEY);

        //初始化公交选项卡的颜色
        gongJiaoLinearLayout.setBackgroundResource(R.color.bottom_lable_color);
        gongJiaoTextView.setTextColor(getResources().getColor(R.color.white_my));
        dianPuLocationEditText.setText(shopModel.getShopName());

        //设置点击事件
        gongJiaoLinearLayout.setOnClickListener(this);
        jiaCheLinearLayout.setOnClickListener(this);
        buXingLinearLayout.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_gong_jiao_search_way://公交
                reset();
                gongJiaoLinearLayout.setBackgroundResource(R.color.bottom_lable_color);
                gongJiaoTextView.setTextColor(getResources().getColor(R.color.white_my));
                break;
            case R.id.ll_jia_che_search_way://驾车
                reset();
                jiaCheLinearLayout.setBackgroundResource(R.color.bottom_lable_color);
                jiaCheTextView.setTextColor(getResources().getColor(R.color.white_my));
                break;
            case R.id.ll_bu_xing_search_way://步行
                reset();
                buXingLinearLayout.setBackgroundResource(R.color.bottom_lable_color);
                buXingTextView.setTextColor(getResources().getColor(R.color.white_my));
                break;
            case R.id.btn_search_search_way:
                if(gongJiaoTextView.getCurrentTextColor()==getResources().getColor(R.color.white_my)){  //点击了公交
                    gongJiaoResultListView.setVisibility(View.VISIBLE);
                    jiaCheResultListView.setVisibility(View.GONE);
                    gongJiaoXianLuClickChuLi();
                }else if(jiaCheTextView.getCurrentTextColor()==getResources().getColor(R.color.white_my)){  //点击了驾车
                    jiaCheResultListView.setVisibility(View.VISIBLE);
                    gongJiaoResultListView.setVisibility(View.GONE);
                    jiaCheXianLuClickChuLi();
                }else if(buXingTextView.getCurrentTextColor()==getResources().getColor(R.color.white_my)){  //点击了步行
                    Toast.makeText(this,"此功能暂时不支持",Toast.LENGTH_SHORT).show();
                    gongJiaoResultListView.setVisibility(View.GONE);
                    jiaCheResultListView.setVisibility(View.GONE);
                    //buXingXianLuClickChuLi();
                }
                break;
        }
    }



    *//**
     * 公交线路的点击事件处理
     *//*
    private void gongJiaoXianLuClickChuLi() {
        //创建公交线路规划检索实例
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

        //创建公交线路规划检索监听者
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            public void onGetWalkingRouteResult(WalkingRouteResult result) {

            }
            public void onGetTransitRouteResult(final TransitRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {    //说明没有错误
                    if (result.getRouteLines().size() > 0 ) {
                        GongJiaoLuXianGuiHuaAdapter adapter=new GongJiaoLuXianGuiHuaAdapter(SearchWayActivity.this,result.getRouteLines());
                        gongJiaoResultListView.setAdapter(adapter);
                        gongJiaoResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent();
                                intent.putExtra(MyConstant.ROUTE_LINE_KEY, result.getRouteLines().get(position));
                                intent.putExtra(MyConstant.JIAO_TONG_KEY,MyConstant.GONG_JIAO_JIAO_TONG);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

                    } else {
                        MyLog.d("transitresult", "结果数<0" );
                        Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                //
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        //设置公交线路规划检索监听者
        mSearch.setOnGetRoutePlanResultListener(listener);

        //准备检索起、终点信息
        PlanNode stNode = PlanNode.withLocation(myPosition);
        PlanNode enNode = PlanNode.withLocation(dianPuPosition);

        //发起公交线路规划检索
        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city("武汉")
                .to(enNode));

        //释放检索实例
        //mSearch.destroy();

    }




    *//**
     * 驾车的点击事件处理
     *//*
    private void jiaCheXianLuClickChuLi() {
        //创建驾车线路规划检索实例
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

        //创建驾车线路规划检索监听者
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            public void onGetWalkingRouteResult(WalkingRouteResult result) {

            }
            public void onGetTransitRouteResult(TransitRouteResult result) {

            }
            public void onGetDrivingRouteResult(final DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {    //说明没有错误
                    if (result.getRouteLines().size() > 0 ) {
                        JiaCheLuXianGuiHuaAdapter adapter=new JiaCheLuXianGuiHuaAdapter(SearchWayActivity.this,result.getRouteLines());
                        jiaCheResultListView.setAdapter(adapter);
                        jiaCheResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent();
                                intent.putExtra(MyConstant.ROUTE_LINE_KEY, result.getRouteLines().get(position));
                                intent.putExtra(MyConstant.JIAO_TONG_KEY,MyConstant.JIA_CHE_JIAO_TONG);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    } else {
                        MyLog.d("transitresult", "结果数<0" );
                        Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        //设置驾车线路规划检索监听者
        mSearch.setOnGetRoutePlanResultListener(listener);

        //准备检索起、终点信息
        PlanNode stNode = PlanNode.withLocation(myPosition);
        PlanNode enNode = PlanNode.withLocation(dianPuPosition);

        //发起驾车线路规划检索
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));

        //释放检索实例
        //mSearch.destroy();
    }



    *//**
     * 步行的点击事件处理
     *//*
    private void buXingXianLuClickChuLi() {
        //创建步行线路规划检索实例
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

        //创建步行线路规划检索监听者
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                MyLog.d(tag,"步行的执行了吗？");
                MyLog.d(tag,"结果的路线条数是:"+result.getRouteLines().size());
                //MyLog.d(tag,"结过是:"+result.getRouteLines().get(0));

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {    //说明没有错误
                    if (result.getRouteLines().size() > 0 ) {
                        MyLog.d(tag,"结果的路线条数是:"+result.getRouteLines().size());
                        Intent intent = new Intent();
                        intent.putExtra(MyConstant.ROUTE_LINE_KEY, result.getRouteLines().get(0));
                        intent.putExtra(MyConstant.JIAO_TONG_KEY,MyConstant.BU_XING_JIAO_TONG);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        MyLog.d("transitresult", "结果数<0" );
                        Toast.makeText(SearchWayActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            public void onGetTransitRouteResult(TransitRouteResult result) {

            }
            public void onGetDrivingRouteResult(final DrivingRouteResult result) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };

        //设置驾车线路规划检索监听者
        mSearch.setOnGetRoutePlanResultListener(listener);

        //准备检索起、终点信息
        PlanNode stNode = PlanNode.withLocation(myPosition);
        PlanNode enNode = PlanNode.withLocation(dianPuPosition);

        //发起驾车线路规划检索
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));

        //释放检索实例
        //mSearch.destroy();
    }





    *//**
     * 重置view的背景
     *//*
    private void reset() {
        gongJiaoLinearLayout.setBackgroundResource(R.color.white_my);
        jiaCheLinearLayout.setBackgroundResource(R.color.white_my);
        buXingLinearLayout.setBackgroundResource(R.color.white_my);
        gongJiaoTextView.setTextColor(getResources().getColor(R.color.black));
        jiaCheTextView.setTextColor(getResources().getColor(R.color.black));
        buXingTextView.setTextColor(getResources().getColor(R.color.black));
    }
*/

}
