package com.example.asus_cp.dongmanbuy.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.ScreenUtils;

/**
 * 自定义的侧滑菜单
 * Created by asus-cp on 2016-05-21.
 */
public class SlidingMenu extends HorizontalScrollView
{
    private String tag="SlidingMenu";
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * dp
     */
    private int mMenuRightPadding;
    /**
     * 菜单的宽度
     */
    private int mMenuWidth;
    private int mHalfMenuWidth;

    private boolean isOpen;

    private boolean once;

    private int flag;//我自定义的，是否是第一次

    public SlidingMenu(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);

    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mScreenWidth = ScreenUtils.getScreenWidth(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.SlidingMenu_rightPadding:
                    // 默认50
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50f,
                                    getResources().getDisplayMetrics()));// 默认为10DP
                    break;
            }
        }
        a.recycle();
    }

    public SlidingMenu(Context context)
    {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**
         * 显示的设置一个宽度
         */
        if (!once)
        {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }


    private float myFirstX =0f;
    private float myLastX=0f;

    /**
     * 触摸事件的处理
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                myFirstX =ev.getX();//当是拦截的，把事件转过来的时候，这边会接收不到down事件，所以在onintercept的方法里面也必须有这个语句
                break;

            case MotionEvent.ACTION_MOVE:
                /*float moveX=ev.getX();
                float moveDeltaX=moveX- myFirstX;
                if(moveX-myLastX>0){
                    this.smoothScrollTo((int) (600-moveDeltaX),0);
                }else{
                    this.smoothScrollTo((int) moveDeltaX,0);
                }
                myLastX=moveX;*/
                break;
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                float scroolX=getScrollX();
                float x=ev.getX();
                float deltaX=x- myFirstX;
                if(deltaX>150){
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                    MyLog.d(tag,"处理从左往右滑动"+isOpen);
                }else{
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                }
                return true;
        }
        return super.onTouchEvent(ev);
        /*int action = ev.getAction();
        float firstX=0;
        switch (action)
        {
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                MyLog.d(tag,"滚动的距离"+scrollX+"");
                if (scrollX <-100) {    //没用
                    this.smoothScrollTo(-200, 0);
                    isOpen = false;
                    MyLog.d(tag,"-200执行了吗");
                } else if(scrollX==0){
                    this.smoothScrollTo(600, 0);//关闭侧滑
                    isOpen = false;
                    MyLog.d(tag,"600执行了吗");
                }
                else {
                    this.smoothScrollTo(0, 0);//打开侧滑
                    isOpen = true;
                    MyLog.d(tag,"0执行了吗");
                }
                return false;
        }
        return false;*/
    }



    /**
     * 拦截事件的方法,最外层的不拦截任何事件
     *
     */
    private float interceptedX=0;//这个要定义成员变量
    private float interceptedY=0;//这个要定义成员变量
    private float firstX=0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted=false;
        float x=0;
        float y=0;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted=false;
                interceptedX=ev.getX();
                interceptedY=ev.getY();
                myFirstX =ev.getX();//拦截的会先获取到事件
                break;
            case MotionEvent.ACTION_MOVE:
//                if(isOpen){
//                    return true;
//                }
                x=ev.getX();
                y=ev.getY();
                float deltaX=x-interceptedX;
                float deltaY=y-interceptedY;
                MyLog.d(tag,"x="+x+"..............."+"interceptedX="+interceptedX);
                if(deltaX>150 && Math.abs(deltaX)>Math.abs(deltaY)){   //从左往右滑动,要划出菜单
                    intercepted=true;
                    MyLog.d(tag, "拦截从左往右滑动");
                }else {
                    intercepted=false;
                    //MyLog.d(tag,"侧滑从左往右滑动");
                }

                MyLog.d(tag,isOpen+"");

                if(isOpen){
                    MyLog.d(tag, "执行了吗");
                    //flag++;
                    if(deltaX<-150 && Math.abs(deltaX)>Math.abs(deltaY)){   //从左往右滑动,要划出菜单
                        intercepted=true;
                        MyLog.d(tag, "拦截从右往左滑动");
                    }else {
                        intercepted=false;
                        //MyLog.d(tag,"侧滑从左往右滑动");
                    }
                }
//                interceptedX=x;
//                interceptedY=y;
                break;
            case MotionEvent.ACTION_UP:
//                if(!isOpen){
//                    intercepted=true;
//                }

                break;
        }
        return intercepted;
    }



    /**
     * 打开菜单
     */
    public void openMenu()
    {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu()
    {
        if (isOpen)
        {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle()
    {
        if (isOpen)
        {
            closeMenu();
        } else
        {
            openMenu();
        }
    }


}

