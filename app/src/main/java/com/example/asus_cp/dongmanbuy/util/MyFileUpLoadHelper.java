package com.example.asus_cp.dongmanbuy.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传的帮助类
 * Created by asus-cp on 2016-10-31.
 */
public class MyFileUpLoadHelper {
    private static final String TAG = "MyFileUpLoadHelper";
    public static final String CHAR_SET_MY="utf-8";
    public static final String DENG="=";
    public static final String YU="&";
    public static final String MULTIPART_FROM_DATA="multipart/form-data";
    public static final String BOUNDARY=java.util.UUID.randomUUID().toString();
    public static final String LINE_END ="\r\n";
    public static final String PREFIX="--";


   /* --ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC
    Content-Disposition: form-data;name="desc"
    Content-Type: text/plain; charset=UTF-8
    Content-Transfer-Encoding: 8bit

    [......][......][......][......]...........................
            --ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC
    Content-Disposition: form-data;name="pic"; filename="photo.jpg"
    Content-Type: application/octet-stream
    Content-Transfer-Encoding: binary

    [图片二进制数据]
            --ZnGpDtePMx0KrHh_G0X99Yef9r8JZsRJSXC--
*/


    /**
     * 上传的方法
     * @param urlString
     * @param params
     * @param files
     * @return 服务器返回的结果
     */
    public static String upLoadFile(String urlString,Map<String,String> params,Map<String,File> files){
        String result=null;
        HttpURLConnection conn=null;
        URL url=null;
        try {
            url=new URL(urlString);
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setChunkedStreamingMode(0);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", CHAR_SET_MY);
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            //发送参数数据
            StringBuilder sb=new StringBuilder();
            Set<Map.Entry<String,String>> paramEntry=params.entrySet();
            for(Map.Entry<String,String> entry:paramEntry){
                sb.append(PREFIX+BOUNDARY+LINE_END);
                sb.append("Content-Disposition: form-data;name="+entry.getKey()+LINE_END);
                sb.append("Content-Type: text/plain; charset=UTF-8"+LINE_END);
                sb.append("Content-Transfer-Encoding: 8bit"+LINE_END);
                sb.append(LINE_END);
                sb.append(entry.getValue()+LINE_END);
            }
            OutputStream out=conn.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(out,CHAR_SET_MY));
            BufferedOutputStream bos=new BufferedOutputStream(out);
            bw.write(sb.toString());
            bw.flush();

            //发送文件数据
            Set<Map.Entry<String,File>> fileSet=files.entrySet();
            for(Map.Entry<String,File> entry:fileSet){
                bw.write(PREFIX+BOUNDARY+LINE_END);
                bw.write("Content-Disposition: form-data;name="+entry.getKey()+"; filename="+entry.getValue().getName()+LINE_END);
                bw.write("Content-Type: application/octet-stream; charset="+CHAR_SET_MY+LINE_END);
                bw.write("Content-Transfer-Encoding: binary"+LINE_END);
                bw.write(LINE_END);
                bw.flush();
                FileInputStream fileInputStream=new FileInputStream(entry.getValue());
                byte[] buf=new byte[1024];
                int len=0;
                while((len=fileInputStream.read(buf))!=-1){
                    bos.write(buf,0,len);
                }
                bos.flush();
                bw.write(LINE_END);
                bw.flush();
            }

            //上传请求结束标志
            bw.write(PREFIX+BOUNDARY+PREFIX+LINE_END);
            bw.flush();

            //读取服务端返回的数据
            if(conn.getResponseCode()==200){
                InputStream inputStream=conn.getInputStream();
                byte[] buf=new byte[1024];
                inputStream.read(buf);
                String tempS=new String(buf,CHAR_SET_MY);
                result=tempS.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
