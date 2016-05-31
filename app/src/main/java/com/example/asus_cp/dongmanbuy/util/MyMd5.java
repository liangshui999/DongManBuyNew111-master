package com.example.asus_cp.dongmanbuy.util;

import android.util.Base64;

import java.security.MessageDigest;

/**
 * 主要对密码进行加密，采用md5加密
 * Created by asus-cp on 2016-05-29.
 */
public class MyMd5 {
    public static String md5encode(String s){
        switch (s.length()){
            case 1:
                s="az1568612"+s;
                break;
            case 2:
                s="mnddpkx154"+s;
                break;
            case 3:
                s="mdjnwd7shhjcgv8"+s;
                break;
            case 4:
                s="meinda68dfmlknjv"+s;
                break;
            case 5:
                s="eojllsasn65d8d"+s;
                break;
            case 6:
                s=s+"djskkjc168d53sa6";
                break;
            case 7:
                s=s+"dlslkjceow38nje";
                break;
            case 8:
                s=s+"slmjckszl980w";
                break;
            case 9:
                s=s+"dnuxgs65";
                break;
            case 10:
                s=s+"dnuxd16562";
                break;
            case 11:
                s="15645djkhc"+s;
                break;
            case 12:
                s="15dkcikhc"+s;
                break;
            case 13:
                s="mdinx"+s;
                break;
            case 14:
                s="mdinx"+s+"snkjdnfjd";
                break;
            case 15:
                s="inx"+s+"sdd";
                break;
            case 16:
                s="indsx"+s+"sddlxl";
                break;
            case 17:
                s="d1546"+s+"sddlxl";
                break;
            case 18:
                s="d1546"+s+"sd8923";
                break;
            case 19:
                s="6"+s+"sd8923";
                break;
            case 20:
                s="d1546"+s+"sdd84f6s3";
                break;
        }
        byte[] digestedValue =null;
        String result=null;
        try{
            MessageDigest md =MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            digestedValue = md.digest();
            result=Base64.encodeToString(digestedValue,Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
