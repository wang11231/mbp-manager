package com.art.manager.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Logger sysErrLogger = Logger.getLogger("sysErr");

	public static String RESOURCES_URL_PREFIX;

	public static String DATA_URL_PREFIX;
	public static String ZHONGHUA_PATH;
	
	public static String RESOURCES_STORAGE_PATH;
	
	public static String DATA_STORAGE_PATH;
	
	public static String HOST_CONTEXT_PREFIX;
	
	// TO BE DELETE
	public static String RESOURCES_USERDATA_PREFIX;
	
	public static String RESOURCES_USERDATA_PATH;

	/** 账户中心路径 */
	public static String ACCOUNT_CENTER_URL;

	/** 商户号 */
	public static String WX_MCH_ID;
	public static String WX_APP_ID;
	/** 微信支付回调 URL */
	public static String URL_WX_PAY_NOTIFY;

	public static String WX_SIGN_KEY;
	/** 微信统一下单接口 */
	public static String URL_WX_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/** 微信转账接口 */
	public static String URL_WX_TRANSFERS = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**配置万能验证码*/
	public static  String UNIVERSAL_VERIFICATION_CODE;
    public static String NOTIFY_URL;

	// TODO 删除
	/** 短地址服务前缀 */
	@Deprecated
	public static String SURL_PREFIX;
	public static String PARTNER_IMG_PATH;

    public static  String XIN_CHENG_KEY_PAY;
    public static String XIN_CHENG_WSDL_PATH;
    public static String XIN_CHENG_WSDL_PATH_TRACK;
    public static String XIN_CHENG_CRYPTO;
    public static String XIN_CHENG_CRYPTO_TRACK;
    public static String XIN_CHENG_PAY_KEY;
    public static String XIN_CHENG_PAY_SUCCESS_URL;
    public static String XIN_CHENG_PAY_NOTIFY_URL;
    public static String XIN_CHENG_USERNAME;
    public static String CERTIFY_PATH;

	static {
		Properties properties = new Properties();
		try {
			properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			sysErrLogger.error("严重的ERROR!!!!! 系统启动错误，无法读取系统配置文件！！！！", e);
		}

		// 初始化配置
		RESOURCES_URL_PREFIX = properties.getProperty("RESOURCES_URL_PREFIX");
		WX_MCH_ID = properties.getProperty("WX_MCH_ID");
		WX_APP_ID = properties.getProperty("WX_APP_ID");
		URL_WX_PAY_NOTIFY = properties.getProperty("URL_WX_PAY_NOTIFY");
		WX_SIGN_KEY = properties.getProperty("WX_SIGN_KEY");
		RESOURCES_STORAGE_PATH = properties.getProperty("RESOURCES_STORAGE_PATH");
		DATA_URL_PREFIX = properties.getProperty("DATA_URL_PREFIX");
		DATA_STORAGE_PATH = properties.getProperty("DATA_STORAGE_PATH");
		HOST_CONTEXT_PREFIX = properties.getProperty("HOST_CONTEXT_PREFIX");
		
		RESOURCES_USERDATA_PREFIX = properties.getProperty("RESOURCES_USERDATA_PREFIX");
		RESOURCES_USERDATA_PATH = properties.getProperty("RESOURCES_USERDATA_PATH");
		ZHONGHUA_PATH = properties.getProperty("ZHONGHUA_PATH");

		ACCOUNT_CENTER_URL = properties.getProperty("ACCOUNT_CENTER_URL");

		UNIVERSAL_VERIFICATION_CODE = properties.getProperty("UNIVERSAL_VERIFICATION_CODE");
		PARTNER_IMG_PATH = properties.getProperty("PARTNER_IMG_PATH");
        NOTIFY_URL = properties.getProperty("NOTIFY_URL");
		SURL_PREFIX = HOST_CONTEXT_PREFIX + "/surl?u=";
        XIN_CHENG_KEY_PAY = properties.getProperty("XIN_CHENG_KEY_PAY");
        XIN_CHENG_WSDL_PATH = properties.getProperty("XIN_CHENG_WSDL_PATH");
        XIN_CHENG_WSDL_PATH_TRACK = properties.getProperty("XIN_CHENG_WSDL_PATH_TRACK");
        XIN_CHENG_CRYPTO = properties.getProperty("XIN_CHENG_CRYPTO");
        XIN_CHENG_CRYPTO_TRACK = properties.getProperty("XIN_CHENG_CRYPTO_TRACK");
        XIN_CHENG_PAY_KEY = properties.getProperty("XIN_CHENG_PAY_KEY");
        XIN_CHENG_PAY_SUCCESS_URL = properties.getProperty("XIN_CHENG_PAY_SUCCESS_URL");
        XIN_CHENG_PAY_NOTIFY_URL = properties.getProperty("XIN_CHENG_PAY_NOTIFY_URL");
        XIN_CHENG_USERNAME = properties.getProperty("XIN_CHENG_USERNAME");
        CERTIFY_PATH = properties.getProperty("CERTIFY_PATH");
	}
}
