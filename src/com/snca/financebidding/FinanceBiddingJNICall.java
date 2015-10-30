package com.snca.financebidding;

public class FinanceBiddingJNICall {
	public static final String partitionRight = "@";

	public static final String partitionLeft = "#";
	static {
		System.loadLibrary("SnCAJCSignEncry");
	}

	public native static String generateRandomString(int length)
			throws Exception;

	public native static String dataSign(String in) throws Exception;

	public native static boolean signatureValidation(String desKey,
			String signature) throws Exception;

	public native static String encryptZipFile(String zipFilePath,
			String enFilePath, String key, int type) throws Exception;

	public native static String decryptZipFile(String enFilePath,
			String zipFilePath, String key, int type) throws Exception;

	public native static String lockAttachment(String mixtureStr,
			String hostPublicKey) throws Exception;

	public native static String unlockAttachment(String mixtureStrEncode,
			String sn) throws Exception;

	public native static String DESEncrypt(String randomString11, String mixStr)
			throws Exception;

	public native static String DESDecrypt(String sn, String mixtureStrEncode)
			throws Exception;

	public native static String getHostKeySerialNumber(String hostPublicKey)
			throws Exception;

	public native static String getCertInfoNocustbase64Cert(String hostPublicKey)
			throws Exception;

	public native static String getHostPublicKey() throws Exception;
}
