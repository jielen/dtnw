package com.snca.financebidding;

import java.io.File;
import java.io.FileOutputStream;

public class FileTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File f = new File("D:\\aa.en");
		String s = "abcdefg┌dsafsdafsad";
		String sn = "1234567890abcdef";
		FileOutputStream out = new FileOutputStream(f);
		out.write((s + sn).getBytes());
	}
}
