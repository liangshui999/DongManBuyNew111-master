package com.example.asus_cp.dongmanbuy.activity.gou_wu;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.activity.BaseActivity;
import com.example.asus_cp.dongmanbuy.adapter.OrderCommentImageAdapter;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.customview.MyGridViewA;

import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 订单评价的界面
 * Created by asus-cp on 2016-10-26.
 */
public class DingDanCommentActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "DingDanCommentActivity";

    private TextView goodNameTextView;
    private ImageView goodPicImageView;
    private EditText commetEditText;
    private MyGridViewA myGridViewA;
    private Button comimitButton;
    private TextView selectCameraTextView;

    private List<String> paths;
    private OrderCommentImageAdapter adapter;
    private String path;//手指点击位置对应的图片path
    private int mPositon;//手指点击位置对应的position

    public static final int REQUEST_CAMERA_CODE=1;
    public static final int REQUEST_PREVIEW_CODE=2;
    public static final int PERMISSION_CODE_CAMERA=11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_ding_dan_comment_layout);
        setTitle(R.string.order_comment);
        initView();
        init();
    }


    @Override
    public void initView() {
        goodNameTextView= (TextView) findViewById(R.id.text_good_name_order_comment);
        goodPicImageView= (ImageView) findViewById(R.id.img_good_pic_order_comment);
        commetEditText= (EditText) findViewById(R.id.edit_text_order_comment);
        myGridViewA= (MyGridViewA) findViewById(R.id.grid_view_order_comment);
        comimitButton= (Button) findViewById(R.id.btn_commit_comment_order_comment);
        selectCameraTextView= (TextView) findViewById(R.id.text_add_pic_order_comment);

        //设置点击事件
        comimitButton.setOnClickListener(this);
        selectCameraTextView.setOnClickListener(this);


    }


    /**
     * 初始化的方法
     */
    private void init() {
        paths=new ArrayList<>();
        adapter=new OrderCommentImageAdapter(this,paths);
        myGridViewA.setAdapter(adapter);

        myGridViewA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(DingDanCommentActivity.this,CommentPicPreviewActivity.class);
                intent.putExtra(MyConstant.PREVIEW_PATHS_KEY, (Serializable) paths);
                intent.putExtra(MyConstant.PREVIEW_INIT_POSTION_KEY,position);
                startActivity(intent);

            }
        });
    }


    /**
     * 开启相册选择图片，或者是开启相机
     */
    private void startCamera() {
        MultiImageSelector.create(this)
                .showCamera(true) // show camera or not. true by default
        .count(5) // max select image size, 9 by default. used width #.multi()
                .multi() // multi mode, default mode;
                .origin((ArrayList<String>) paths) // original select data set, used width #.multi()
                .start(this, REQUEST_CAMERA_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(PERMISSION_CODE_CAMERA)
    public void requestCameraSuccess(){
        startCamera();
    }

    @PermissionDenied(PERMISSION_CODE_CAMERA)
    public void requestCameraDenied(){
        Toast.makeText(DingDanCommentActivity.this, "需要打开摄像头的权限才能使用，请到设置中设置", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CAMERA_CODE:
                if(data!=null){
                    List<String> temp=data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    paths.clear();
                    paths.addAll(temp);
                    adapter.notifyDataSetChanged();
                    for(int i=0;i<paths.size();i++){
                        MyLog.d(TAG,"path="+paths.get(i));
                    }
                }
                break;
            case REQUEST_PREVIEW_CODE:

                break;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_add_pic_order_comment://选择照片
                MPermissions.requestPermissions(DingDanCommentActivity.this,PERMISSION_CODE_CAMERA, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);//请求相机的权限
                break;
            case R.id.btn_commit_comment_order_comment://提交评论
                break;
        }
    }
}
