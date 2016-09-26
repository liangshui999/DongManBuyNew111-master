# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# 继承自Serializable的所有类
-keepclassmembers class * implements java.io.Serializable {
  *;
}


#支付宝部分的混淆规则,注意下面的时间要和libs里面的文件的时间对应
#-libraryjars libs/alipaySingle-20160516.jar

#为了避免打包不成功添加上去的
-dontwarn com.alipay.**
-dontwarn com.handmark.pulltorefresh.**
-dontwarn com.jeremyfeinstein.slidingmenu.**


-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


#上拉刷新，下拉加载框架
-keep class com.handmark.pulltorefresh.**{*;}

#圆形imagview里面的所有内容
-keep class de.hdodenhof.circleimageview.**{*;}

#新浪微博里面的所有内容
-keep class com.sina.**{*;}

#侧滑菜单的所有内容
-keep class com.jeremyfeinstein.slidingmenu.**{*;}

#gson的所有内容
-keep class com.google.gson.**{*;}

# Gson uses generic type information stored in a class file when working with fields. Proguard  
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

-keep class sun.misc.Unsafe {*;}

-keep class com.google.gson.examples.android.model.** {*;}

#和gson相关的model里面的内容
-keep class com.example.asus_cp.dongmanbuy.model.my_json_model.** {*;}

#zxing下面的所有内容
-keep class com.google.zxing.**{*;}

#友盟统计的混淆规则
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.example.asus_cp.dongmanbuy.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}





