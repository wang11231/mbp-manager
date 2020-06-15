package com.art.manager.util;

import com.art.manager.pojo.Msg;
import com.github.pagehelper.PageHelper;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ooxml.util.DocumentHelper.newDocumentBuilder;

/**
 * 公共的方法
 */
public class CommonUtils {

	/**
	 * 判断Map传参有没有分页数据，并分页
	 *
	 * @param params
	 * @return
	 */
	public static Msg isPageData(Map<String, Object> params) {

		Msg msg = new Msg();
		//获取分页数据
		if (StringUtils.isEmpty(params.get("pageNum"))) {
			return new Msg(Msg.FAILURE_CODE, "请填写初始页pageNum");
		}
		if (StringUtils.isEmpty(params.get("pageSize"))) {
			return new Msg(Msg.FAILURE_CODE, "请填写每页的宽度pageSize");
		}
		Integer pageNum;
		Integer pageSize;
		try {
			pageNum = Integer.parseInt(String.valueOf(params.get("pageNum")));
			pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
		} catch (NumberFormatException e) {
			return new Msg(Msg.FAILURE_CODE, "分页数据不是数字格式");
		}
		PageHelper.startPage(pageNum, pageSize);
		msg.setCode(Msg.SUCCESS_CODE);
		return msg;
	}

	/**
	 * 通过流来读取请求体
	 */
	public static String getRequestBody(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			ServletInputStream in = request.getInputStream();
			byte[] bytes = new byte[1024];
			int len;
			while ((len = in.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, len));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static boolean isTestEnv(Environment env){
		List<String> list = Arrays.asList(env.getActiveProfiles());

		if(list.size() == 0){
			return true;
		}
		if(list.contains("test")){
			return true;
		}
		return false;
	}

	/**
	 * 处理pageNum和pageSize
	 * @param map
	 * @param key
	 * @return
	 */
	public static Integer str2Integer(Map<String, Object> map, String key) {
		Object o = map.get(key);
		Integer value = null;
		if (o != null && o.toString().length() > 0) {
			value = new BigInteger(o.toString()).intValue();
		}

		return value;
	}
}
