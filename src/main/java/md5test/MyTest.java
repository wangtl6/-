package md5test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import md5test.util.MD5Util;

public class MyTest {
	static String file = "E:/综合资料库沉淀-核心库-方案库-产品库/003各分支项目、系统组事项、部门、人员/安徽移动/安徽移动前后端分离==新资料/00空间申请计算，会话序列化真实导出/会话序列化结果文件/file_session_n054E9662B5F37C8BA3B5E33D42300098-1";
	static Map<String, String> md5AndSessionStr = new HashMap<String, String>();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// 读取安徽实际序列化的会话为基准：21K
		String sessionStr = IOUtils.toString(new FileInputStream(file));
		// // 相同字符串，计算多次MD5串是否会不同
		// check1(sessionStr);
		// // 修改字符串局部，计算多次MD5串是否会相同
		check2(sessionStr);

	}

	private static void check1(String sessionStr) {
		String md5Str1 = MD5Util.md5(sessionStr);
		for (int i = 0; i < 100; i++) {// 共计算100万次，分100轮完成，每轮1万次
			System.out.println("轮次=》" + i);
			for (int j = 0; j < 10000; j++) {
				String md5Str2 = MD5Util.md5(sessionStr);
				if (!md5Str1.equals(md5Str2)) {
					System.err.println("算出不同的值了！");
				}
			}
		}
		System.out.println("计算完成！");
	}

	static int num = 0;

	private static void check2(String sessionStr) {
		// 随机修改字符串中某个字符
		String md5Str = MD5Util.md5(sessionStr);
		md5AndSessionStr.put(md5Str, sessionStr);
		for (int i = 0; i < sessionStr.length(); i++) {// 将字符串从头到尾轮训替换
//			System.out.println("轮次=》" + i);
			String newSessionStr = doReplace(sessionStr, i);// 命中的字符修改为'x'
			String newMd5Str = MD5Util.md5(newSessionStr);
			if (md5AndSessionStr.containsKey(newMd5Str)) {
				System.out.println("有重复,个数：" + (num++) + ",总长度为：sessionStr=" + sessionStr.length() + ",newSessionStr=" + newSessionStr.length());
				// System.err.println("有重复，原始串："+md5AndSessionStr.get(newMd5Str)+",计算后串："+newSessionStr);
			} else {
				md5AndSessionStr.put(newMd5Str, sessionStr);
			}
		}
		System.out.println("计算完成！");
	}

	static int replaceNum=1;
	private static String doReplace(String sessionStr, int i) {
		int end = i - 1;
		end = end <= 0 ? 0 : end;
		int begin = i + replaceNum-1;
		begin = (begin >= sessionStr.length()) ? sessionStr.length() - 1 : begin;
		// 替换两位
		String to="";
		for (int j = 0; j < replaceNum; j++) {
			to+="x";
		}
		return sessionStr.substring(0, end) + to + sessionStr.substring(begin);
	}
}