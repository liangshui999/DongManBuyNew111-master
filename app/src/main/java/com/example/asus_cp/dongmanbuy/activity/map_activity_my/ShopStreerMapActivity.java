package com.example.asus_cp.dongmanbuy.activity.map_activity_my;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.adapter.baidu_adapter.RouteLineAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.ShopModel;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.List;

/**
 * 展示店铺地址的界面，主要是放一幅地图
 * Created by asus-cp on 2016-07-25.
 */
public class ShopStreerMapActivity extends Activity implements View.OnClickListener{

    private String tag="ShopStreerMapActivity";

    private MapView mMapView;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap baiduMap;//百度地图的控制器
    private int densityDpi;//屏幕像素密度
    private ShopModel shopModel;
    boolean useDefaultIcon = false;
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    TransitRouteResult nowResult = null;
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    private LatLng myPosition;//我的位置
    private LatLng dianPuPosition;//店铺的位置

    public static final int REQUEST_SEARCH_KEY=1;//跳转到查询

    private ImageView daoHangImageView;//导航
    private TextView dianPuNameUpTextView;//店铺上名称
    private TextView dianPuNameDownTextView;//店铺下名称
    private TextView dianPuDetailAddressTextView;//店铺地址
    private TextView gongJiaoXianLuTextView;//公交线路




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop_street_map_activity_layout);

        shopModel=getIntent().getParcelableExtra(MyConstant.SHOP_MODEL_KEY);

        //获取屏幕像素密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap=mMapView.getMap();//获取控制器
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();//开始定位

        //
        daoHangImageView= (ImageView) findViewById(R.id.img_dao_hang_map);
        dianPuNameUpTextView= (TextView) findViewById(R.id.text_dian_pu_name_up_map);
        dianPuNameDownTextView= (TextView) findViewById(R.id.text_dian_pu_name_down_map);
        dianPuDetailAddressTextView= (TextView) findViewById(R.id.text_dian_pu_detail_di_zhi_map);
        gongJiaoXianLuTextView= (TextView) findViewById(R.id.text_gong_jiao_xian_lu_map);


        //给view设置初值
        dianPuNameUpTextView.setText(shopModel.getShopName());
        dianPuNameDownTextView.setText(shopModel.getShopName());
        dianPuDetailAddressTextView.setText(shopModel.getShopAddress());

        //给view设置点击事件
        daoHangImageView.setOnClickListener(this);
        gongJiaoXianLuTextView.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        //不用的时候取消
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    /**
     * 定位的初始化设置
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_dao_hang_map:
                finish();
                break;
            case R.id.text_gong_jiao_xian_lu_map:
                //Toast.makeText(this,"点击了公交线路",Toast.LENGTH_SHORT ).show();
                //gongJiaoXianLuClickChuLi();
                Intent searchIntent=new Intent(this,SearchWayActivity.class);
                searchIntent.putExtra(MyConstant.MY_LOCATION_KEY,myPosition);
                searchIntent.putExtra(MyConstant.DIAN_PU_LOCATION_KEY,dianPuPosition);
                searchIntent.putExtra(MyConstant.SHOP_MODEL_KEY,shopModel);
                startActivityForResult(searchIntent, REQUEST_SEARCH_KEY);
                break;
        }
    }


    /**
     * 公交线路的点击事件处理
     */
    private void gongJiaoXianLuClickChuLi() {
        //创建公交线路规划检索实例
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

        //创建公交线路规划检索监听者
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            public void onGetWalkingRouteResult(WalkingRouteResult result) {
                //
            }
            public void onGetTransitRouteResult(TransitRouteResult result) {
                //返回整个路段的详细信息
                for(int i=0;i<result.getRouteLines().size();i++){
                    for(int j=0;j<result.getRouteLines().get(i).getAllStep().size();j++){
                        MyLog.d(tag,"查询的信息是："+result.getRouteLines().get(i).getAllStep().get(j).getInstructions());
                    }
                    MyLog.d(tag,"查询的信息是："+result.getRouteLines().get(0).getAllStep().get(0).getInstructions());
                }
                MyLog.d(tag,"查询的信息是："+result.getRouteLines().get(0).getAllStep().get(0).getInstructions());
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(ShopStreerMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    if (result.getRouteLines().size() > 1 ) {
                        nowResult = result;

                        MyTransitDlg myTransitDlg = new MyTransitDlg(ShopStreerMapActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.TRANSIT_ROUTE);
                        myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                            public void onItemClick(int position) {
                                route = nowResult.getRouteLines().get(position);
                                TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                                baiduMap.setOnMarkerClickListener(overlay);
                                routeOverlay = overlay;
                                overlay.setData(nowResult.getRouteLines().get(position));
                                overlay.addToMap();
                                overlay.zoomToSpan();
                            }

                        });
                        myTransitDlg.show();

                    } else if ( result.getRouteLines().size() == 1 ) {
                        // 直接显示
                        route = result.getRouteLines().get(0);
                        TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                        baiduMap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();

                    } else {
                        MyLog.d("transitresult", "结果数<0" );
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_SEARCH_KEY:    //从搜索界面回来的数据
                if(resultCode==RESULT_OK){
                    baiduMap.clear();
                    RouteLine routeLine=data.getParcelableExtra(MyConstant.ROUTE_LINE_KEY);
                    route = routeLine;
                    TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                    baiduMap.setOnMarkerClickListener(overlay);
                    routeOverlay = overlay;
                    overlay.setData((TransitRouteLine) routeLine);
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
                break;
        }

    }






    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick( int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List< ? extends RouteLine> transitRouteLines,  RouteLineAdapter.Type
                type) {
            this( context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new  RouteLineAdapter( context, mtransitRouteLines , type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick( position);
//                    mBtnPre.setVisibility(View.VISIBLE);
//                    mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster( OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }




    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public  MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.map);
        }


    }













    /**
     * 位置监听器
     */
    class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(final BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            //我自己添加的内容
            // 构造定位数据
            /*MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//
                    .direction(location.getDirection())// 方向
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);*/

            //设置定位的配置信息
            final BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.mipmap.map);
            /*MyLocationConfiguration myLocationConfiguration=new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,true,bitmapDescriptor);
            baiduMap.setMyLocationEnabled(true);//必须设置这个，不设置这个，定位图标根本不会显示
            baiduMap.setMyLocationConfigeration(myLocationConfiguration);
*/


            // 定义marker坐标点
            myPosition = new LatLng(location.getLatitude(), location.getLongitude());
            // 构建markerOption，用于在地图上添加marker
            OverlayOptions options = new MarkerOptions()//
                    .position(myPosition)// 设置marker的位置
                    .icon(bitmapDescriptor)// 设置marker的图标
                    .zIndex(9)// 設置marker的所在層級
                    .draggable(true);// 设置手势拖拽
            // 在地图上添加marker，并显示
            Marker marker = (Marker) baiduMap.addOverlay(options);




            //添加文字注释
            TextOptions textOptions = new TextOptions();
            textOptions.bgColor(0xAAFFFF00)  //設置文字覆蓋物背景顏色
                    .fontSize(20*densityDpi/160)  //设置字体大小
                    .fontColor(0xFFFF00FF)// 设置字体颜色
                    .text("我")  //文字内容
                    .rotate(0)  //设置文字的旋转角度
                    .position(myPosition);// 设置位置
            baiduMap.addOverlay(textOptions);


            //百度地图地理编码，根据具体地址获取经纬度（百度坐标）
            GeoCoder geoCoder = GeoCoder.newInstance();
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {   //地理编码返回的结果
                    MyLog.d(tag,"地理编码的结果是："+geoCodeResult.getLocation().toString());

                    // 定义marker坐标点
                    dianPuPosition = new LatLng(geoCodeResult.getLocation().latitude,
                            geoCodeResult.getLocation().longitude);

                    // 构建markerOption，用于在地图上添加marker
                    OverlayOptions options = new MarkerOptions()//
                            .position(dianPuPosition)// 设置marker的位置
                            .icon(bitmapDescriptor)// 设置marker的图标
                            .zIndex(9)// 設置marker的所在層級
                            .draggable(true);// 设置手势拖拽
                    // 在地图上添加marker，并显示
                    Marker marker = (Marker) baiduMap.addOverlay(options);



                    //添加文字注释
                    TextOptions textOptions = new TextOptions();
                    textOptions.bgColor(0xAAFFFF00)  //設置文字覆蓋物背景顏色
                            .fontSize(20*densityDpi/160)  //设置字体大小
                            .fontColor(0xFFFF00FF)// 设置字体颜色
                            .text("店铺")  //文字内容
                            .rotate(0)  //设置文字的旋转角度
                            .position(dianPuPosition);// 设置位置
                    baiduMap.addOverlay(textOptions);
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                }
            });

            GeoCodeOption geoCodeOption=new GeoCodeOption();
            geoCodeOption.city("武汉市");
            geoCodeOption.address(shopModel.getShopAddress());
            geoCoder.geocode(geoCodeOption);



            //将地图移动到定位的点
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(msu);

            //释放地理编码对象
            //geoCoder.destroy();


            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            MyLog.d(tag, sb.toString());

        }
    }
}
