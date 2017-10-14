package com.com.tool;

public class MyURL {

	//public static final String HIP = "http://192.168.0.150:8080/pdf_2017-09-14";
	public static final String HIP = "http://10.26.230.148:8080/pdf_2017-09-14";
	/**
	 * -------------------------请求错误的标志----------------------------------
	 */

	/**密码错误3**/
	public static final int MSG_PWD_ERROR=3;

	public static final String SEARCHOREDE = HIP+"/orderandroid.jhtm";//查询是否有待处理订单及查询订单

	public static final String LOGIN = HIP+"/loginandroid.jhtm";//登陆请求
	public static final String WAITORDER = HIP+"/orderandroid.jhtm";//查询是否有待处理订单
	public static final String UPLOADADDRESS = HIP+"/upload";//服务器上传地址
	public static final String DOWNLOADFILE= "http://10.26.230.148:8080/pdf_file/";//文件下载地址
	public static final String ALLEDITORER= HIP+"/editandroid.jhtm";//获得所有的编辑人员
	public static final String COMPlET= HIP+"/orderandroid.jhtm";//获得所有编辑过的文件
}
