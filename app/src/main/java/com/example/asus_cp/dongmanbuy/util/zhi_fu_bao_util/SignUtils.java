package com.example.asus_cp.dongmanbuy.util.zhi_fu_bao_util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}



	/**

	 * RSA验签名检查

	 * @param content 待签名数据

	 * @param sign 签名值

	 * @param ali_public_key 支付宝公钥


	 * @return 布尔值

	 */

	public static boolean verify(String content, String sign, String ali_public_key){
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			//PublicKey pubKey=getPublicKey(encodedKey);
			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 从公钥数据取得公钥
	 *
	 * @param bPubKeyInput
	 * @return
	 */
	public static PublicKey getPublicKey(byte[] bPubKeyInput) {
		PublicKey rsaPubKey = null;
		byte[] bX509PubKeyHeader = { 48, -127, -97, 48, 13, 6, 9, 42, -122, 72,
				-122, -9, 13, 1, 1, 1, 5, 0, 3, -127, -115, 0 };
		try {
			byte[] bPubKey = new byte[bPubKeyInput.length
					+ bX509PubKeyHeader.length];
			System.arraycopy(bX509PubKeyHeader, 0, bPubKey, 0,
					bX509PubKeyHeader.length);
			System.arraycopy(bPubKeyInput, 0, bPubKey,
					bX509PubKeyHeader.length, bPubKeyInput.length);

			X509EncodedKeySpec rsaKeySpec = new X509EncodedKeySpec(bPubKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			rsaPubKey = keyFactory.generatePublic(rsaKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsaPubKey;
	}



	/**
	 * 解密
	 *
	 * @param content       密文
	 * @param private_key   商户私钥
	 * @param input_charset 编码格式
	 * @return 解密后的字符串
	 */

	public static String decrypt(String content, String private_key, String input_charset) throws Exception {

		PrivateKey prikey = getPrivateKey(private_key);

		Cipher cipher = Cipher.getInstance("RSA");

		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));

		ByteArrayOutputStream writer = new ByteArrayOutputStream();

		//rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密

		byte[] buf = new byte[128];
		int bufl;
		while ((bufl = ins.read(buf)) != -1) {

			byte[] block = null;


			if (buf.length == bufl) {

				block = buf;

			} else {

				block = new byte[bufl];

				for (int i = 0; i < bufl; i++) {

					block[i] = buf[i];

				}

			}


			writer.write(cipher.doFinal(block));

		}


		return new String(writer.toByteArray(), input_charset);

	}


	/**
	 * 得到私钥
	 *
	 * @param key 密钥字符串（经过base64编码）
	 * @throws Exception
	 */

	public static PrivateKey getPrivateKey(String key) throws Exception {


		byte[] keyBytes;


		keyBytes = Base64.decode(key);


		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);


		KeyFactory keyFactory = KeyFactory.getInstance("RSA");


		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);


		return privateKey;

	}

}


