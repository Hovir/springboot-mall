package com.organization.utils;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 字符、字符串数组、字符串等相关工具类
 *
 * @author ming
 * @date 2018-01-17
 */
public class StringUtil {

	//联通 - 查询手机卡数
	public final static String LT = "中国联通";
	//移动- 查询手机卡数
	public final static String YD = "中国移动";
	//电信 - 查询手机卡数
	public final static String DX = "中国电信";

	/**
	 * 判断字符串是否为null、空字符串、或者是多个空字符串。
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 判断字符串是否不为null、空字符串、或者是多个空字符串。
	 */
	public static boolean isNotEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为UUID(此处UUID为36位,格式为"8-4-4-4-16"的字符串)
	 */
	public static boolean isUUID(String str) {
		if (str == null) {
			return false;
		}
		if (str.length() != 36) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串不为空，且长度小于等于最大值
	 */
	public static boolean isLtLength(String str, int maxLength) {
		if (isNotEmpty(str) && str.trim().length() <= maxLength) {
			return true;
		}
		return false;
	}

	/**
	 * 获取长度为32的UUID字符串
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	/**
	 * 替换掉HTML标签方法
	 * 
	 * @param html
	 * @return
	 */
	public static String replaceHtml(String html) {
		if (isEmpty(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String string = m.replaceAll("");
		return string;
	}

	/**
	 * 密码生产器
	 * 
	 * @param pwd_len
	 * @return
	 */
	public static String genRandomPwd(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");
			Matcher m = regex.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判获取当前系统的日期和时间戳
	 * 
	 * @auto MadiSon
	 * @date 2018/08/06
	 **/
	public static String getTimeStamp() {
		return String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000);
	}

	/**
	 * 判断字符串是否为纯数字数字
	 * 
	 * @auto MadiSon
	 * @date 2018/08/06
	 **/
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断手机号运营商
	 * 
	 * @auto MadiSon
	 * @date 2018/10/17
	 * @update 2018/10/18
	 */
	public static String mobileSupplier(String mobile) {
		
		if(StringUtil.isEmpty(mobile)){
			return "手机号不能为空";
		}

		Map<String, String> map = new HashMap<>();
		map.put("isChinaMobile", "");
		map.put("isChinaUnion", "");
		map.put("isChinaTelcom", "");

		try {
			ClassLoader classLoader = StringUtil.class.getClassLoader();
			URL resource = classLoader.getResource("common/mobileSupplier.xml");
			String path = resource.getPath();

			File f = new File(path);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList nl = doc.getElementsByTagName("info");
			for (int i = 0; i < nl.getLength(); i++) {
				String name = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
				String value = doc.getElementsByTagName("value").item(i).getFirstChild().getNodeValue();
				map.put(name, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		String mobileSupplier = "未知";

		if (Pattern.matches(map.get("isChinaMobile"), mobile)) {
			mobileSupplier = YD;
		} else if (Pattern.matches(map.get("isChinaUnion"), mobile)) {
			mobileSupplier = LT;
		} else if (Pattern.matches(map.get("isChinaTelcom"), mobile)) {
			mobileSupplier = DX;
		}
		return mobileSupplier;
	}

}
