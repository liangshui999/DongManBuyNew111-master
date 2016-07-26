package com.example.asuscp.dongmanbuy.util;

public class ZhiFuBaoHelper {
	
	static{
		System.loadLibrary("zhifubaoqianmingmy");
	}
	
	public native String getPid();//
	public native String getSeller();//
	public native String getPrivateYao();//
	public native String getPublicYao();//
	

}
