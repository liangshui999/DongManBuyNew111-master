package com.example.asus_cp.dongmanbuy.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.example.asus_cp.dongmanbuy.util.MyLog;
import com.example.asus_cp.dongmanbuy.util.MyScreenInfoHelper;

/**
 * 自定义的imagerequest，主要用于商品详情的图片的完整显示
 * Created by asus-cp on 2016-09-08.
 */
public class MyImageRequest extends ImageRequest{
    private String tag="MyImageRequest";
    private ImageView imageView;

    private OnImageSizeGetListener onImageSizeGetListener;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();
            layoutParams.width=msg.arg1;
            layoutParams.height=msg.arg2;
            imageView.setLayoutParams(layoutParams);
            if(onImageSizeGetListener != null){
                onImageSizeGetListener.onImageSizeGeted(msg.arg1, msg.arg2);//这句代码是我加的
            }
        }
    };

    public MyImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }

    public MyImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener,ImageView imageView) {
        super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
        this.imageView=imageView;
    }

    public MyImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                          Bitmap.Config decodeConfig, Response.ErrorListener errorListener,
                          ImageView imageView, OnImageSizeGetListener onImageSizeGetListener) {
        super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
        this.imageView=imageView;
        this.onImageSizeGetListener = onImageSizeGetListener;
    }

    @Override
    public Response<Bitmap> doParse(NetworkResponse response) {
        byte[] data = response.data;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;

        // If we have to resize this image, first get the natural bounds.
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;

        // Then compute the dimensions we would ideally like to decode to.
        int desiredWidth = MyScreenInfoHelper.getScreenWidth();//我改的主要就是这两句代码，其他都没有改变
        int desiredHeight = desiredWidth*actualHeight/actualWidth;


        Message message=handler.obtainMessage(1);
        message.arg1=desiredWidth;
        message.arg2=desiredHeight;
        handler.sendMessage(message);

        MyLog.d(tag,"actualWidth="+actualWidth+"........"+"actualHeight="+actualHeight);
        MyLog.d(tag,"desiredWidth="+desiredWidth+"........"+"desiredHeight="+desiredHeight);

        // Decode to the nearest power of two scaling factor.
        decodeOptions.inJustDecodeBounds = false;
        // TODO(ficus): Do we need this or is it okay since API 8 doesn't support it?
        //decodeOptions.inPreferQualityOverSpeed = PREFER_QUALITY_OVER_SPEED;
        decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);

        Bitmap tempBitmap =
                BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);

        // If necessary, scale down to the maximal acceptable size.
        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
                tempBitmap.getHeight() > desiredHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap,
                    desiredWidth, desiredHeight, true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }


        if (bitmap == null) {
            return Response.error(new ParseError(response));
        } else {
            return Response.success(bitmap, HttpHeaderParser.parseCacheHeaders(response));
        }
    }
}
