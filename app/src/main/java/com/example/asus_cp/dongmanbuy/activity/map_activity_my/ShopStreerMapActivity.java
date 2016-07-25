package com.example.asus_cp.dongmanbuy.activity.map_activity_my;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

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
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import java.util.List;

/**
 * 展示店铺地址的界面，主要是放一幅地图
 * Created by asus-cp on 2016-07-25.
 */
public class ShopStreerMapActivity extends Activity{

    private String tag="ShopStreerMapActivity";

    private MapView mMapView;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap baiduMap;//百度地图的控制器



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop_street_map_activity_layout);
        mMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap=mMapView.getMap();//获取控制器
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        mLocationClient.start();
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
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//
                    .direction(location.getDirection())// 方向
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);

            //设置定位的配置信息
            final BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.mipmap.map);
            MyLocationConfiguration myLocationConfiguration=new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,true,bitmapDescriptor);
            baiduMap.setMyLocationEnabled(true);//必须设置这个，不设置这个，定位图标根本不会显示
            baiduMap.setMyLocationConfigeration(myLocationConfiguration);


            //添加文字注释
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            TextOptions textOptions = new TextOptions();
            textOptions.bgColor(0xAAFFFF00)  //設置文字覆蓋物背景顏色
                    .fontSize(28)  //设置字体大小
                    .fontColor(0xFFFF00FF)// 设置字体颜色
                    .text("我在这里啊！！！！")  //文字内容
                    .rotate(-30)  //设置文字的旋转角度
                    .position(latLng);// 设置位置
            baiduMap.addOverlay(textOptions);


            //百度地图地理编码，根据具体地址获取经纬度（百度坐标）
            GeoCoder geoCoder = GeoCoder.newInstance();
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {   //地理编码返回的结果
                    MyLog.d(tag,"地理编码的结果是："+geoCodeResult.getLocation().toString());

                    // 定义marker坐标点
                    LatLng point = new LatLng(geoCodeResult.getLocation().latitude,
                            geoCodeResult.getLocation().longitude);

                    // 构建markerOption，用于在地图上添加marker
                    OverlayOptions options = new MarkerOptions()//
                            .position(point)// 设置marker的位置
                            .icon(bitmapDescriptor)// 设置marker的图标
                            .zIndex(9)// 設置marker的所在層級
                            .draggable(true);// 设置手势拖拽
                    // 在地图上添加marker，并显示
                    Marker marker = (Marker) baiduMap.addOverlay(options);



                    //添加文字注释
                    LatLng latLng = new LatLng(geoCodeResult.getLocation().latitude,
                            geoCodeResult.getLocation().longitude );
                    TextOptions textOptions = new TextOptions();
                    textOptions.bgColor(0xAAFFFF00)  //設置文字覆蓋物背景顏色
                            .fontSize(28)  //设置字体大小
                            .fontColor(0xFFFF00FF)// 设置字体颜色
                            .text("店铺在这里")  //文字内容
                            .rotate(-30)  //设置文字的旋转角度
                            .position(latLng);// 设置位置
                    baiduMap.addOverlay(textOptions);
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                }
            });

            GeoCodeOption geoCodeOption=new GeoCodeOption();
            geoCodeOption.city("武汉市");
            geoCodeOption.address("汉口北小商品城");
            geoCoder.geocode(geoCodeOption);



            //将地图移动到定位的点
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(msu);







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
