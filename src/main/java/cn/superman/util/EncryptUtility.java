package cn.superman.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

public class EncryptUtility {
	private EncryptUtility() {
		throw new AssertionError("不要实例化工具类哦");
	}

	/**
	 * 生成md5
	 * 
	 * @param message
	 * @return
	 */
	public static String Md5Encoding(String message) {
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes();

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			return bytesToHex(buff);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("加密失败：" + e.getMessage());
		}
	}

	/**
	 * 二进制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder md5str = new StringBuilder();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	/**
	 * 进行加密
	 * 
	 * @param seed
	 *            为加密时和解密时需要用的对应匹配密匙
	 * @param data
	 *            就是需要加密的数据了
	 * @return
	 * @throws Exception
	 */
	public static String AESEncoding(String seed, String data) throws Exception {

		return Base64Utils.encodeToString(encrypt(getRawKey(seed.getBytes()),
				data.getBytes()));
	}

	/**
	 * 进行解密
	 * 
	 * @param seed为加密时和解密时需要用的对应匹配密匙
	 * @param data就是需要加密的数据了
	 * @return
	 * @throws Exception
	 */
	public static String AESDencoding(String seed, String data)
			throws Exception {
		return new String(dencrypt(getRawKey(seed.getBytes()),
				Base64Utils.decodeFromString(data)));
	}

	// 获取密匙并进行编码
	private static byte[] getRawKey(byte[] seed) throws Exception {
		// 获取密匙生成器
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(seed);
		// 生成128位的AES密码生成器(还有192,256位)
		generator.init(128, secureRandom);
		// 生成密匙
		SecretKey key = generator.generateKey();
		// 返回字节码,以供需要
		return key.getEncoded();
	}

	// 加密
	private static byte[] encrypt(byte[] key, byte[] data) throws Exception {
		// 生成一组密匙
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// 用Cipher.ENCRYPT_MODE模式，secretKeySpec编码组，生成AES的加密方法
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		// 得到加密后的数据
		return cipher.doFinal(data);
	}

	// 解密,基本上和加密写法是一样的，就是变换了一个模式
	private static byte[] dencrypt(byte[] key, byte[] data) throws Exception {
		// 生成一组密匙
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// 用Cipher.DECRYPT_MODE模式，secretKeySpec编码组，生成AES的加密方法
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		// 得到加密后的数据
		return cipher.doFinal(data);
	}

}
