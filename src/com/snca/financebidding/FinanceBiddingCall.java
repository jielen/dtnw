package com.snca.financebidding;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.ufgov.zc.client.zc.ztb.JobThreads;
import com.ufgov.zc.common.zc.filemstools.FileSign;

public class FinanceBiddingCall extends FinanceBiddingJNICall {
	static {
		Iterator it = System.getProperties().keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println(System.getProperties().get(key));
		}
	}

	public static void main3(String[] args) throws Exception {
		try {
			String zipFilePath = "d://ca_test//50M.doc";
			String zipFilePath2 = "d://ca_test//50Md.doc";
			String zipEnFilePath = "d://ca_test//50M.doc.en";
			String attFilePath = "d://ca_test//50M.doc.en.att";
			Map paras = new HashMap();
			FinanceBiddingCall demo = new FinanceBiddingCall();
			demo.biddingWithProviderEncodingOnly(paras, zipFilePath,
					zipEnFilePath);
			JOptionPane.showMessageDialog(null, "加密完成，将进行解密...");
			// Map result =
			// demo.bidOpeningForProviderOnlyProviderEncoded(attFilePath,
			// "11111111");
			Map result = demo.bidOpeningForProviderOnlyProviderEncoded(paras,
					attFilePath);
			demo.bidOpenningForServerForLocal(zipEnFilePath, zipFilePath2,
					result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getMergedPublicKey(String pubKey1, String pubKey2) {
		return pubKey1 + partitionLeft + pubKey2;
	}

	public static String generateRandom(int length) {
		if (length < 0) {
			length = 128;
		}
		String sourceBuffer = new String(
				"ABCDEFGHxyz=/IJKLM0156+abcdefghi234NOPQRSTUVWXYZjklmnopqrstuvw789");
		StringBuffer result = new StringBuffer();
		Random rand = new Random();
		for (int i = 0; i < length; ++i) {
			result.append(sourceBuffer.charAt(rand.nextInt(sourceBuffer
					.length())));
		}
		return result.toString();
	}

	public Map bidding(Map paras, String zipFilePath, String zipEnFilePath)
			throws Exception {
		Map returnValues = new HashMap();
		String randomString128 = "";
		String providerPasswd = "";
		String signatureP7 = "";
		String ProviderPublicKey = "";
		String hostPublicKeyA = "";
		String hostPublicKeyB = "";
		String hostPublicKeySNA = "";
		String hostPublicKeySNB = "";
		String digitalEnvelopeA0 = "";
		String digitalEnvelopeA1 = "";
		String digitalEnvelopeA2 = "";
		String digitalEnvelopeB0 = "";
		String digitalEnvelopeB1 = "";
		String digitalEnvelopeB2 = "";
		String digitalEnvelopeProvider = "";
		String CertInfoNocustA = "";
		String CertInfoNocustB = "";
		String ProviderCertInfoNocust = "";
		String Md5 = "";

		// 0 原文的MD5值
		// try {
		Md5 = (String) paras.get("ORIGTBFILEMD5");
		// signatureP7 = dataSign(Md5);
		// if (null == signatureP7 || "".equals(signatureP7)) {
		// throw new RuntimeException();
		// }
		// } catch (Exception e) {
		// throw new RuntimeException("产生签名错误，请重试！", e);
		// }
		returnValues.put("ORIGTBFILEMD5", Md5);
		// returnValues.put("ORIGTBFILEMD5SIGNATURE", signatureP7);

		// 2、拆分公钥列表，获得主副KEY公钥
		// 公钥1
		JobThreads.publishProgressMsg("正在用公钥信息获取客服信任号A...");
		hostPublicKeyA = (String) paras.get("MASTERPUBLICKEYA");
		CertInfoNocustA = (String) paras.get("MASTERTRUSTCODEA");
		if (CertInfoNocustA == null || "".equals(CertInfoNocustA)) {
			CertInfoNocustA = getCertInfoNocustbase64Cert(hostPublicKeyA);
		}
		try {
			if ((CertInfoNocustA == null) || ("".equals(CertInfoNocustA))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("无客服信任号，请重试！", e);
		}
		returnValues.put("MASTERPUBLICKEYA", hostPublicKeyA);
		returnValues.put("MASTERTRUSTCODEA", CertInfoNocustA);
		// 公钥1
		JobThreads.publishProgressMsg("正在用公钥信息获取客服信任号B...");
		hostPublicKeyB = (String) paras.get("MASTERPUBLICKEYB");
		CertInfoNocustB = (String) paras.get("MASTERTRUSTCODEB");
		if (CertInfoNocustB == null || "".equals(CertInfoNocustB)) {
			CertInfoNocustB = getCertInfoNocustbase64Cert(hostPublicKeyB);
		}
		try {
			if ((CertInfoNocustB == null) || ("".equals(CertInfoNocustB))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("无客服信任号，请重试！", e);
		}
		returnValues.put("MASTERPUBLICKEYB", hostPublicKeyB);
		returnValues.put("MASTERTRUSTCODEB", CertInfoNocustB);
		// 2 得到加密字符串。
		try {
			randomString128 = generateRandomString(128);
			// randomString128 = generateRandom(128);
			if ((randomString128 == null) || ("".equals(randomString128))) {
				throw new RuntimeException();
			}
			randomString128 = randomString128.trim();
		} catch (Exception e) {
			throw new RuntimeException("产生随机密码错误，请重试！", e);
		}

		// 3 得到用户口令。
		JobThreads.publishProgressMsg("正在等待供应商输入口令...");
		providerPasswd = getProviderInputPassword();
		providerPasswd = providerPasswd.trim();
		if ("".equals(providerPasswd)) {
			returnValues.put("ENCODEDSTATUS", "ignore");
			returnValues.put("FAILREASON", "用户取消了对标书的加密！");
			return returnValues;
		}

		// 4 检查客户端传过来的会议主持人的 主key公钥证书是否正确
		// 分别检查主副KEY
		if (hostPublicKeyA != null && !"".equals(hostPublicKeyA)) {
			JobThreads.publishProgressMsg("正在验证检查公钥A...");
			try {
				hostPublicKeySNA = getHostKeySerialNumber(hostPublicKeyA);
				if ((hostPublicKeySNA == null) || ("".equals(hostPublicKeySNA))) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				throw new RuntimeException("会议主持人的公钥A证书校验错误，请重试！", e);
			}
		}
		if (hostPublicKeyB != null && !"".equals(hostPublicKeyB)) {
			JobThreads.publishProgressMsg("正在验证检查公钥B...");
			try {
				hostPublicKeySNB = getHostKeySerialNumber(hostPublicKeyB);
				if ((hostPublicKeySNB == null) || ("".equals(hostPublicKeySNB))) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				throw new RuntimeException("会议主持人的公钥B证书校验错误，请重试！", e);
			}
		}
		// 5 制作数字信封
		JobThreads.publishProgressMsg("正在进行文件加密...");
		try {
			digitalEnvelopeA0 = lockAttachment(randomString128 + partitionRight
					+ signatureP7, hostPublicKeyA);
			if ((digitalEnvelopeA0 == null) || ("".equals(digitalEnvelopeA0))) {
				throw new RuntimeException();
			}
			digitalEnvelopeB0 = lockAttachment(randomString128 + partitionRight
					+ signatureP7, hostPublicKeyB);
			if ((digitalEnvelopeB0 == null) || ("".equals(digitalEnvelopeB0))) {
				throw new RuntimeException();
			}
			digitalEnvelopeA1 = digitalEnvelopeA0 + partitionRight
					+ CertInfoNocustA;
			digitalEnvelopeB1 = digitalEnvelopeB0 + partitionRight
					+ CertInfoNocustB;
		} catch (Exception e) {
			throw new RuntimeException("产生数字信封错误，请重试！", e);
		}

		// 7 用供应商输入的口令加密数字信封
		// 对主key加密后的结果再次加密
		try {
			digitalEnvelopeA2 = FinanceBiddingJNICall1.StringEncryDES(
					digitalEnvelopeA1, providerPasswd);
			// digitalEnvelopeA2 = DESEncrypt(providerPasswd,
			// digitalEnvelopeA1);
			if ((digitalEnvelopeA2 == null) || ("".equals(digitalEnvelopeA2))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("产生数字信封错误，请重试！", e);
		}
		// 对辅key加密后的结果进行再次加密
		try {
			digitalEnvelopeB2 = FinanceBiddingJNICall1.StringEncryDES(
					digitalEnvelopeB1, providerPasswd);
			// digitalEnvelopeB2 = DESEncrypt(providerPasswd,
			// digitalEnvelopeB1);
			if ((digitalEnvelopeB2 == null) || ("".equals(digitalEnvelopeB2))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("产生数字信封错误，请重试！", e);
		}
		returnValues.put("DOUBLEENCODEDDIGITALENVELOPEA", digitalEnvelopeA2);
		returnValues.put("DOUBLEENCODEDDIGITALENVELOPEB", digitalEnvelopeB2);

		// 8 加密随机口令
		try {
			String tmpPK = (String) paras.get("SUPPLIERPUBLICKEY");
			if (tmpPK != null && !"".equals(tmpPK)) {
				ProviderPublicKey = tmpPK;
			} else {
				ProviderPublicKey = getHostPublicKey();
				ProviderPublicKey = ProviderPublicKey.replaceAll("\r\n", "")
						.replaceAll("\n", "");
			}
			ProviderCertInfoNocust = getCertInfoNocustbase64Cert(ProviderPublicKey);
			try {
				if ((ProviderCertInfoNocust == null)
						|| ("".equals(ProviderCertInfoNocust))) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				throw new RuntimeException("无客服信任号，请重试！", e);
			}
			digitalEnvelopeProvider = lockAttachment(providerPasswd,
					ProviderPublicKey);
			if ((digitalEnvelopeProvider == null)
					|| ("".equals(digitalEnvelopeProvider))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("加密口令时错误，请重试！", e);
		}
		returnValues.put("SUPPLIERPUBLICKEY", ProviderPublicKey);
		returnValues.put("SUPPLIERTRUSTCODE", ProviderCertInfoNocust);
		returnValues.put("SUPPLIERPUBKEYENCODEDDIGITALENVELOPE",
				digitalEnvelopeProvider);

		// 9 用随机密钥加密压缩包
		// 0 加密不删除原压缩包
		// 1 加密删除原压缩包
		try {
			String isSucc = FileSign.encrypt(zipFilePath, zipEnFilePath,
					randomString128);
			if ((isSucc == null) || (!isSucc.equals("TRUE"))) {
				if (isSucc.equals("-20")) {
					throw new RuntimeException("传入的文件找不到或文件已经存在，请检查！");
				}
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("加密压缩包错误，请重试！", e);
		}
		returnValues.put("LOCALFILEFULLPATH", zipEnFilePath);
		JobThreads.publishProgressMsg("文件加密顺利完成...");

		return returnValues;
	}

	public Map biddingWithProviderEncodingOnly(Map paras, String zipFilePath,
			String zipEnFilePath) throws Exception {
		Map returnValues = new HashMap();
		String randomString128 = "";
		String providerPasswd = "";
		String signatureP7 = "";
		String providerPubKey = "";
		String digitalEnvelope = "";
		String digitalEnvelope1 = "";
		String CertInfoNocust1 = "";
		String Md5 = "";
		// 0 原文的MD5值
		// try {
		Md5 = (String) paras.get("ORIGTBFILEMD5");
		// if (Md5 == null) {
		// throw new Exception("缺少标书原始文件的MD5码，无法进行文件签名.");
		// }
		// JOptionPane.showMessageDialog(null, "请选择您的CA数字证书(U-KEY)!(当前第一次).");
		// signatureP7 = dataSign(Md5);
		// if (null == signatureP7 || "".equals(signatureP7)) {
		// throw new RuntimeException();
		// }
		// } catch (Exception e) {
		// throw new RuntimeException("产生签名错误，请重试！", e);
		// }
		returnValues.put("ORIGTBFILEMD5", Md5);
		// returnValues.put("ORIGTBFILEMD5SIGNATURE", signatureP7);

		// 1 得到加密字符串。
		try {
			randomString128 = generateRandomString(128);
			// randomString128 = generateRandom(128);
			if (null == randomString128 || "".equals(randomString128)) {
				throw new RuntimeException();
			}
			randomString128 = randomString128.trim();
		} catch (Exception e) {
			throw new RuntimeException("产生随机密码错误，请重试！", e);
		}
		// 2 得到用户口令。
		JobThreads.publishProgressMsg("正在等待供应商输入口令...");
		providerPasswd = getProviderInputPassword();
		providerPasswd = providerPasswd.trim();
		if ("".equals(providerPasswd)) {
			returnValues.put("ENCODEDSTATUS", "ignore");
			returnValues.put("FAILREASON", "用户取消了对标书的加密！");
			return returnValues;
		}
		// 3 得到摘要的签名
		// try {
		// JOptionPane.showMessageDialog(null, "请再次选择您的CA数字证书(U-KEY)!(当前第二次)");
		// signatureP7 = dataSign(randomString128);
		// if (null == signatureP7 || "".equals(signatureP7)) {
		// throw new RuntimeException();
		// }
		// } catch (Exception e) {
		// throw new RuntimeException("产生签名错误，请重试！", e);
		// }
		JobThreads.publishProgressMsg("正在进行文件加密...");
		// 4 制作数字信封
		try {
			digitalEnvelope = FinanceBiddingJNICall1.StringEncryDES(
					randomString128 + partitionRight + signatureP7,
					providerPasswd);
			// digitalEnvelope = DESEncrypt(providerPasswd, randomString128 +
			// partitionRight + signatureP7);
			if (null == digitalEnvelope || "".equals(digitalEnvelope)) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("产生数字信封错误，请重试！", e);
		}
		returnValues.put("SUPPLIERPASSWDENCODEDDIGITALENVELOPE",
				digitalEnvelope);
		// 5 加密随机口令
		try {
			String tmpPK = (String) paras.get("SUPPLIERPUBLICKEY");
			if (tmpPK != null && !"".equals(tmpPK)) {
				providerPubKey = tmpPK;
			} else {
				providerPubKey = getHostPublicKey();
				providerPubKey = providerPubKey.replaceAll("\r\n", "")
						.replaceAll("\n", "");
			}
			CertInfoNocust1 = getCertInfoNocustbase64Cert(providerPubKey);
			try {
				if (null == CertInfoNocust1 || "".equals(CertInfoNocust1)) {
					throw new RuntimeException();
				}
			} catch (Exception e) {
				throw new RuntimeException("无客服信任号，请重试！", e);
			}
			digitalEnvelope1 = lockAttachment(providerPasswd, providerPubKey);
			if (null == digitalEnvelope1 || "".equals(digitalEnvelope1)) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("加密口令时错误，请重试！", e);
		}
		returnValues.put("SUPPLIERPUBLICKEY", providerPubKey);
		returnValues.put("SUPPLIERTRUSTCODE", CertInfoNocust1);
		returnValues.put("SUPPLIERPUBKEYENCODEDDIGITALENVELOPE",
				digitalEnvelope1);

		// 8 用随机密钥加密压缩包
		// 0 加密不删除原压缩包
		// 1加密删除原压缩包
		try {
			String isSucc = FileSign.encrypt(zipFilePath, zipEnFilePath,
					randomString128);
			if (!(null != isSucc && isSucc.equals("TRUE"))) {
				if (isSucc.equals("-20")) {
					throw new RuntimeException("传入的文件找不到或文件已经存在，请检查！");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("加密压缩包错误，请重试！", e);
		}
		returnValues.put("LOCALFILEFULLPATH", zipEnFilePath);
		JobThreads.publishProgressMsg("文件加密顺利完成...");

		return returnValues;
	}

	private String getProviderInputPassword() {
		StringBuffer buff = new StringBuffer("您好，请输入一个密码用以加密您的投标书，请仔细阅读以下内容：");
		buff.append("\n1、密码最好由数字和字母等混合组成，且长度在8~128字节之间，长度越长越好;");
		buff.append("\n2、必须谨慎保存该密码，如果您的【CA数字证书】损坏或丢失，可以使用该密码解密标书;");
		buff.append("\n3、点击【取消】按钮将会终止整个加密及上传过程.");
		final JPasswordField pwd = new JPasswordField();
		final JPasswordField pwd2 = new JPasswordField();
		final JLabel tipLabel = new JLabel("当前密码长度：");
		pwd.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int len = pwd.getPassword().length + 1;
				tipLabel.setText("当前密码长度：" + len + ".");
				if (len >= 8) {
					pwd2.setEditable(true);
				} else {
					pwd2.setEditable(false);
				}
			}
		});
		pwd2.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char[] chars1 = pwd.getPassword();
				char[] chars2 = pwd2.getPassword();
				if (chars2.length < chars1.length) {
					for (int j = 0; j < chars2.length; j++) {
						if (chars1[j] != chars2[j]) {
							tipLabel.setText("两个密码【不匹配】...");
						} else {
							tipLabel.setText("已输入部分【匹配】...");
						}
					}
				} else {
					if (new String(chars1).equals(new String(chars2))) {
						tipLabel.setText("两个密码【相同】...");
					} else {
						tipLabel.setText("两个密码【不】相同！");
					}
				}
			}
		});
		pwd2.setEditable(false);
		tipLabel.setPreferredSize(new Dimension(200, 20));
		Object[] message = { buff.toString(), pwd, "请再次确认：", pwd2, tipLabel };
		int sel = JOptionPane.showConfirmDialog(null, message, "请输入密码",
				JOptionPane.OK_CANCEL_OPTION, 3);
		if (sel == JOptionPane.CANCEL_OPTION) {
			return "";
		}

		String password = new String(pwd.getPassword());
		String password2 = new String(pwd2.getPassword());
		if ((password == null) || ("".equals(password))) {
			JOptionPane.showMessageDialog(null, "密码不能为空！", "提示", 3);
			password = getProviderInputPassword();
		} else if (password.length() < 8) {
			JOptionPane.showMessageDialog(null, "密码长度小于8个字节！");
			password = getProviderInputPassword();
		} else if (password.length() > 128) {
			JOptionPane.showMessageDialog(null, "密码长度不能超过128字节！");
			password = getProviderInputPassword();
		}
		if (!password.equals(password2)) {
			JOptionPane.showMessageDialog(null, "前后两个密码不一致！", "提示", 3);
			password = getProviderInputPassword();
		}
		return password.trim();
	}

	/**
	 * 如果采购中心和供应商共同加密时，对应的解密方法
	 * 
	 * @param paras
	 * @param userPassword
	 * @return
	 * @throws Exception
	 */
	public Map bidOpeningForProvider(Map paras, String userPassword)
			throws Exception {
		String dataInAttContentP = "";
		String digitalEnvelopeA = "";
		String digitalEnvelopeB = "";
		String digitalEnvelopeP = "";
		String CertInfoNocustP = "";
		String dataInAttContentA = "";
		String dataInAttContentB = "";
		String resultAfterPubKeyDecoded = "";
		String CertInfoNocustA = "";
		String CertInfoNocustB = "";
		String dataInAttContentA0 = "";
		String dataInAttContentB0 = "";
		// 获得供应商公钥加密口令后的结果
		dataInAttContentP = (String) paras
				.get("SUPPLIERPUBKEYENCODEDDIGITALENVELOPE");
		CertInfoNocustP = (String) paras.get("SUPPLIERTRUSTCODE");
		JobThreads.publishProgressMsg("正在进行文件解密...");
		if ((userPassword == null) || ("".equals(userPassword))) {
			try {
				digitalEnvelopeP = unlockAttachment(dataInAttContentP,
						CertInfoNocustP);
			} catch (Exception e) {
				throw new RuntimeException("拆解数字信封，得到用户公钥信息错误，请重试！", e);
			}
			if (digitalEnvelopeP != null) {
				if ("-1".equals(digitalEnvelopeP.trim())) {
					throw new RuntimeException("已选择的用户【证书不正确】，请重新选择证书！");
				}
				if ("".equals(digitalEnvelopeP)) {
					throw new RuntimeException("拆解数字信封错误，可能是选择证书有误，请重试！");
				}
			} else {
				throw new RuntimeException("拆解数字信封，得到用户公钥信息错误，请重试！");
			}
			resultAfterPubKeyDecoded = digitalEnvelopeP.trim();
		} else {
			resultAfterPubKeyDecoded = userPassword;
		}
		boolean flag = false;
		Map retValue = new HashMap();
		// 获得主辅KEY被供应商口令加密后的结果
		dataInAttContentA = (String) paras.get("DOUBLEENCODEDDIGITALENVELOPEA");
		if (dataInAttContentA != null && !"".equals(dataInAttContentA)) {
			try {
				digitalEnvelopeA = FinanceBiddingJNICall1.StringDecryptDES(
						dataInAttContentA, resultAfterPubKeyDecoded);
			} catch (Exception e) {
				throw new RuntimeException("拆解数字信封错误，请重试！", e);
			}
			if (digitalEnvelopeA == null || "".equals(digitalEnvelopeA)) {
				throw new RuntimeException("拆解数字信封错误，证书或者密码不正确！");
			}
			String[] decodeRes = digitalEnvelopeA.split(partitionRight);
			dataInAttContentA0 = decodeRes[0].trim();
			CertInfoNocustA = decodeRes[1].trim();
			retValue.put("MASTERPUBKEYAENCODEDDATA", dataInAttContentA0);
			retValue.put("CERTINFONOCUSTA", CertInfoNocustA);
			flag = true;
		}
		dataInAttContentB = (String) paras.get("DOUBLEENCODEDDIGITALENVELOPEB");
		if (dataInAttContentB != null && !"".equals(dataInAttContentB)) {
			try {
				digitalEnvelopeB = FinanceBiddingJNICall1.StringDecryptDES(
						dataInAttContentB, resultAfterPubKeyDecoded);
			} catch (Exception e) {
				throw new RuntimeException("拆解数字信封错误，请重试！", e);
			}
			if (digitalEnvelopeB == null || "".equals(digitalEnvelopeB)) {
				throw new RuntimeException("拆解数字信封错误，证书或者密码不正确！");
			}
			String[] decodedRes = digitalEnvelopeB.split(partitionRight);
			dataInAttContentB0 = decodedRes[0].trim();
			CertInfoNocustB = decodedRes[1].trim();
			retValue.put("MASTERPUBKEYBENCODEDDATA", dataInAttContentB0);
			retValue.put("CERTINFONOCUSTB", CertInfoNocustB);
			flag = true;
		}
		if (!flag) {
			throw new RuntimeException("拆解数字信封时错误，请检查数字信封内容！");
		}
		JobThreads.publishProgressMsg("文件解密顺利完成...");

		return retValue;
	}

	/**
	 * 供应商单独加密时，对应的开标解密流程
	 */
	public Map bidOpeningForProviderOnlyProviderEncoded(Map paras,
			String userPassword) throws Exception {
		String dataInAttContentPK = "";
		String dataInAttContentPW = "";
		String digitalEnvelope = "";
		String finishDecodedEnvelope = "";
		String CertInfoNocust = "";
		String resultAfterPubKeyDecoded = "";
		// 1 拆解文件
		dataInAttContentPK = (String) paras
				.get("SUPPLIERPUBKEYENCODEDDIGITALENVELOPE");
		dataInAttContentPW = (String) paras
				.get("SUPPLIERPASSWDENCODEDDIGITALENVELOPE");
		CertInfoNocust = (String) paras.get("SUPPLIERTRUSTCODE");

		// 2 获得用户口令
		JobThreads.publishProgressMsg("正在进行供应商相关文件解密...");
		if (userPassword == null || "".equals(userPassword)) {
			try {
				digitalEnvelope = unlockAttachment(dataInAttContentPK,
						CertInfoNocust);
			} catch (Exception e) {
				throw new RuntimeException("拆解数字信封，得到用户公钥信息错误，请重试！", e);
			}
			if (null != digitalEnvelope) {
				if ("-1".equals(digitalEnvelope.trim())) {
					throw new RuntimeException("所选择的用户【证书不正确】，请重新选择证书！");
				}
				if ("".equals(digitalEnvelope)) {
					throw new RuntimeException("拆解数字信封错误，可能是选择证书有误，请重试！");
				}
			} else {
				throw new RuntimeException("拆解数字信封，得到用户公钥信息错误，请重试！");
			}
			resultAfterPubKeyDecoded = digitalEnvelope.trim();
		} else {
			resultAfterPubKeyDecoded = userPassword;
		}

		// 3 拆解数字信封
		try {
			finishDecodedEnvelope = FinanceBiddingJNICall1.StringDecryptDES(
					resultAfterPubKeyDecoded, dataInAttContentPW);
		} catch (Exception e) {
			throw new RuntimeException("拆解数字信封错误，证书或者密码不正确！", e);
		}
		if (finishDecodedEnvelope == null || "".equals(finishDecodedEnvelope)) {
			throw new RuntimeException("拆解数字信封错误，证书或者密码不正确！");
		}
		String randomString = finishDecodedEnvelope.split(partitionRight)[0]
				.trim();
		// String signature =
		// finishDecodedEnvelope.split(partitionRight)[1].trim();
		Map retValue = new HashMap();
		retValue.put("RANDOMSTRING", randomString);
		retValue.put("SUPPLIERTRUSTCODE", CertInfoNocust);
		// retValue.put("RANDOMSTRINGSIGNATURE", signature);
		JobThreads.publishProgressMsg("供应商相关文件解密顺利完成...");

		return retValue;
	}

	/**
	 * 主持人主辅key解密
	 * 
	 * @param parasMap
	 * @return
	 * @throws Exception
	 */
	public Map bidOpeningForMaster(Map parasMap) throws Exception {
		String randomString128 = "";
		// String signature = "";
		String digitalEnvelopeA = "";
		String digitalEnvelopeB = "";
		String CertInfoNocustA = "";
		String CertInfoNocustB = "";
		String masterPubKeyAEncodedData = "";
		String masterPubKeyBEncodedData = "";

		masterPubKeyAEncodedData = (String) parasMap
				.get("MASTERPUBKEYAENCODEDDATA");
		CertInfoNocustA = (String) parasMap.get("CERTINFONOCUSTA");
		masterPubKeyBEncodedData = (String) parasMap
				.get("MASTERPUBKEYBENCODEDDATA");
		CertInfoNocustB = (String) parasMap.get("CERTINFONOCUSTB");
		JobThreads.publishProgressMsg("正在进行主持人相关文件解密...");
		try {
			if (masterPubKeyAEncodedData != null
					&& !"".equals(masterPubKeyAEncodedData)) {
				digitalEnvelopeA = unlockAttachment(masterPubKeyAEncodedData,
						CertInfoNocustA);
				if ((!"-1".equals(digitalEnvelopeA.trim()))
						&& (!"".equals(digitalEnvelopeA))) {
					randomString128 = digitalEnvelopeA.split(partitionRight)[0]
							.trim();
					// signature =
					// digitalEnvelopeA.split(partitionRight)[1].trim();
				} else {
					if (masterPubKeyBEncodedData != null
							&& !"".equals(masterPubKeyBEncodedData)) {
						digitalEnvelopeB = unlockAttachment(
								masterPubKeyBEncodedData, CertInfoNocustB);
						if ((!"-1".equals(digitalEnvelopeB.trim()))
								&& (!"".equals(digitalEnvelopeB))) {
							randomString128 = digitalEnvelopeB
									.split(partitionRight)[0].trim();
							// signature =
							// digitalEnvelopeB.split(partitionRight)[1].trim();
						} else {
							throw new RuntimeException(
									"拆解数字信封，得到主持人公钥信息错误，请换另一把Key进行重试！");
						}
					} else {
						throw new RuntimeException(
								"拆解数字信封，得到主持人公钥信息错误，请换另一把Key进行重试！");
					}
				}
			} else if (masterPubKeyBEncodedData != null
					&& !"".equals(masterPubKeyBEncodedData)) {
				digitalEnvelopeB = unlockAttachment(masterPubKeyBEncodedData,
						CertInfoNocustB);
				if ((!"-1".equals(digitalEnvelopeB.trim()))
						&& (!"".equals(digitalEnvelopeB))) {
					randomString128 = digitalEnvelopeB.split(partitionRight)[0]
							.trim();
					// signature =
					// digitalEnvelopeB.split(partitionRight)[1].trim();
				} else {
					throw new RuntimeException(
							"拆解数字信封，得到主持人公钥信息错误，请换另一把Key进行重试！");
				}
			} else {
				throw new RuntimeException("拆解数字信封，得到主持人公钥信息错误，请换另一把Key进行重试！");
			}

		} catch (Exception e) {
			throw new RuntimeException("拆解数字信封，得到主持人公钥信息错误，请换另一把Key进行重试！", e);
		}
		Map retValue = new HashMap();
		retValue.put("RANDOMSTRING", randomString128);
		// retValue.put("RANDOMSTRINGSIGNATURE", signature);
		JobThreads.publishProgressMsg("主持人相关文件解密顺利完成...");

		return retValue;
	}

	/**
	 * 在本地环境为windows的平台下进行解密
	 * 
	 * @param zipEnFilePath
	 * @param zipFilePath
	 * @param paras
	 * @return
	 * @throws Exception
	 */
	public boolean bidOpenningForServerForLocal(String zipEnFilePath,
			String zipFilePath, Map paras) throws Exception {
		try {
			String randomString = (String) paras.get("RANDOMSTRING");
			String signature = (String) paras.get("RANDOMSTRINGSIGNATURE");
			if (!doCheckSignature(randomString, signature)) {
				throw new RuntimeException("服务器端还原标书明文失败，请检查解密密钥...");
			}
			String isSucc = FileSign.decrypt(zipEnFilePath, zipFilePath,
					randomString);
			if ((isSucc == null) || (!isSucc.equals("TRUE"))) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			throw new RuntimeException("还原招标明文过称发生错误，请重试！", e);
		}
		return true;
	}

	private boolean doCheckSignature(String randomString, String signature)
			throws Exception {
		return signatureValidation(randomString, signature);
	}
}