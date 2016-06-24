package com.example.asus_cp.dongmanbuy.activity.personal_center.fund_manager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_cp.dongmanbuy.R;

/**
 * 充值界面
 * Created by asus-cp on 2016-06-24.
 */
public class ChongZhiActivity extends Activity implements View.OnClickListener{

    private EditText chongZhiJinEEditeText;//充值金额
    private EditText beiZhuEditeText;//备注
    private RelativeLayout chongZhiFangShiRelativeLayout;//充值方式
    private Button tiJiaoShenQingButton;//提交申请
    private TextView chongZhiFangShiTextView;//充值方式
    private TextView shouXuFeiTextView;//手续费

    private View parentView;//弹出窗口的父窗口
    private LayoutInflater inflater;

    private PopupWindow chongZhiFangShiWindow;
    private CheckBox zhiFuBaoCheckBox;
    private TextView zhiFuBaoShouXuFeiTanChuTextView;//弹出窗口中的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chong_zhi_activity_layout);

        init();
    }

    /**
     * 初始化的方法
     */
    private void init() {

        inflater=LayoutInflater.from(this);
        parentView=inflater.inflate(R.layout.chong_zhi_activity_layout,null);

        chongZhiJinEEditeText= (EditText) findViewById(R.id.edit_chong_zhi_jin_e_chong_zhi);
        beiZhuEditeText= (EditText) findViewById(R.id.edit_bei_zhu_chong_zhi);
        chongZhiFangShiRelativeLayout= (RelativeLayout) findViewById(R.id.re_layout_chong_zhi_fang_shi_chong_zhi);
        tiJiaoShenQingButton= (Button) findViewById(R.id.btn_ti_jiao_shen_qing_chong_zhi);
        chongZhiFangShiTextView= (TextView) findViewById(R.id.text_zhi_fu_fang_shi_chong_zhi);
        shouXuFeiTextView= (TextView) findViewById(R.id.text_shou_xu_fei_chong_zhi);

        //设置点击事件
        chongZhiFangShiRelativeLayout.setOnClickListener(this);
        tiJiaoShenQingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_layout_chong_zhi_fang_shi_chong_zhi://点击了充值方式
                chongZhiFangShiClickChuLi();
                break;
            case R.id.btn_ti_jiao_shen_qing_chong_zhi://点击了提交申请
                String jinE=chongZhiJinEEditeText.getText().toString();
                String chongZhiFangShi=chongZhiFangShiTextView.getText().toString();
                if("".equals(jinE) || jinE.isEmpty()){
                    Toast.makeText(this,"充值金额为空",Toast.LENGTH_SHORT).show();
                }else if("".equals(chongZhiFangShi) || chongZhiFangShi.isEmpty()){
                    Toast.makeText(this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"充值成功",Toast.LENGTH_SHORT).show();
                }
                break;


            //----------充值方式弹出窗口的点击事件-----------------------------------
            case R.id.img_close_chong_zhi_tan_chu://关闭
                chongZhiFangShiWindow.dismiss();
                break;
            case R.id.re_layout_zhi_fu_bao_chong_zhi_fang_shi_tan_chu://点击了支付宝
                String text=zhiFuBaoShouXuFeiTanChuTextView.getText().toString();
                shouXuFeiTextView.setText(text);
                zhiFuBaoCheckBox.setChecked(true);
                break;
        }
    }


    /**
     * 充值方式的点击事件处理
     */
    private void chongZhiFangShiClickChuLi() {
        View chongZhiFangShiView=inflater.inflate(R.layout.chong_zhi_fang_shi_tan_chu_layout,null);
        ImageView closeChongZhiFangShiImageView= (ImageView) chongZhiFangShiView.findViewById(R.id.img_close_chong_zhi_tan_chu);
        RelativeLayout zhiFuBaoRelativeLayout= (RelativeLayout) chongZhiFangShiView.findViewById(R.id.re_layout_zhi_fu_bao_chong_zhi_fang_shi_tan_chu);
        zhiFuBaoCheckBox= (CheckBox) chongZhiFangShiView.findViewById(R.id.check_box_chong_zhi_tan_chu);
        zhiFuBaoShouXuFeiTanChuTextView= (TextView) chongZhiFangShiView.findViewById(R.id.text_zhi_fu_bao_shou_xu_fei_chong_zhi_tan_chu);//支付宝手续费

        closeChongZhiFangShiImageView.setOnClickListener(this);
        zhiFuBaoRelativeLayout.setOnClickListener(this);

        chongZhiFangShiWindow=new PopupWindow(chongZhiFangShiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //外部点击时可以消失
        chongZhiFangShiWindow.setBackgroundDrawable(new ColorDrawable());
        chongZhiFangShiWindow.setOutsideTouchable(true);
        chongZhiFangShiWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);//最后才是show,顺序很重要
        setBackgroundAlpha(0.5f);
        chongZhiFangShiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
