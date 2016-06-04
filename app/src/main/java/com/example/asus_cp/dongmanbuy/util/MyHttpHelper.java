package com.example.asus_cp.dongmanbuy.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 主要用于手动发起post请求
 * Created by asus-cp on 2016-06-04.
 */
public class MyHttpHelper {
    /**
     * 手动发起post请求，此方法作废
     * @param newPassword
     */
    private void shouDongPost(final String newPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn=null;
                try {
                    //String ceShiUrl="http://192.168.1.104:2006";
                    URL url=new URL("");
                    conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "utf-8");
                    OutputStream out=conn.getOutputStream();
                    String content="json=%7B%22username%22%3A%22%22%2C%22email%22%3A%22254304837%40qq.com%22%2C%22email_code%22%3A%22641477%22%2C%22sms_code%22%3A%22%22%2C%22mobile%22%3A%22%22%2C%22new_password%22%3A%2211%22%7D";
                    out.write(content.getBytes());
                    out.flush();
                    out.close();
                    InputStream in=conn.getInputStream();
                    byte[] buf=new byte[1024*1024];
                    in.read(buf);
                    //MyLog.d(tag, new String(buf));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    conn.disconnect();
                }
            }
        }).start();
    }
}
