package cn.superman.web.bean;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 该类可以生成验证码，验证码内容有：数字加英文字母大小写加一些简单的中文字符
 * 
 * @author 梁超人，制作时间：2014.11.21
 */
public class VerificationCode {
	// 数字加英文字母大小写加一些简单的中文字符
	// private String data =
	// "123456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ\u7684\u4e00\u662f\u4e86\u6211\u4e0a\u4eec\u6765\u5230\u65f6\u5927\u5730\u4e3a\u5b50\u4e2d\u4f60\u8bf4\u751f\u56fd\u5e74\u7740\u5c31\u90a3\u548c\u8981\u5979\u51fa\u4e5f\u5f97\u91cc\u540e\u81ea\u4ee5\u4f1a\u5bb6\u53ef\u4e0b\u800c";
	private String data = "123456789abcdefghjkmnpqrstuvwxyz";
	// 图片高度和宽度
	private int height = 35;
	private int width = 70;
	// 验证码的内容
	private String text;
	// 验证码的个数
	private int charLength = 4;
	// 干扰线个数
	private int lineLength = 5;
	private String[] fontNames = { "宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312" };
	private Color bgColor = new Color(255, 255, 255);
	private Random r = new Random();
	// 验证码图片
	private BufferedImage image;

	private VerificationCode() {
		creat();
	}

	private VerificationCode(int height, int width) {
		this.height = height;
		this.width = width;
		creat();
	}

	private VerificationCode(String data, int height, int width,
			int charLength, int lineLength, String[] fontNames, Color bgColor) {
		this.data = data;
		this.height = height;
		this.width = width;
		this.charLength = charLength;
		this.lineLength = lineLength;
		this.fontNames = fontNames;
		this.bgColor = bgColor;
		creat();
	}

	private VerificationCode(int height, int width, int charLength,
			int lineLength) {
		this.height = height;
		this.width = width;
		this.charLength = charLength;
		this.lineLength = lineLength;
		creat();
	}

	private void creat() {
		StringBuilder builder = new StringBuilder();
		char word = 0;
		creatImage();
		drawLine();
		float temp = (width - width / 10) / charLength;

		for (int i = 0; i < charLength; i++) {
			word = randomChar();
			builder.append(word);
			float x = i * 1.0F * temp;
			drawWord(word, x, height / 2 + r.nextInt(height / 3));
		}

		text = builder.toString();
	}

	/**
	 * 返回一个采用默认参数的类，当你获得这个对象实例时，该类内部已经产生了一张图片了
	 * 用户只需要通过getImage方法来获得图片，通过getText来获得验证内容即可，当需要将图片输出到页面时
	 * 只需要调用类中writeImage方法即可
	 * 
	 * @return CreatVerificationCode
	 */
	public static VerificationCode getDefaultNewinstance() {
		return new VerificationCode();
	}

	/**
	 * 返回一个可以指定图片宽高,验证码个数，干扰线条数目的类，当你获得这个对象实例时，该类内部已经产生了一张图片了
	 * 用户只需要通过getImage方法来获得图片，通过getText来获得验证内容即可，当需要将图片输出到页面时
	 * 
	 * @param height
	 *            图片高度
	 * @param width
	 *            图片宽度
	 * @param charLength
	 *            验证码个数
	 * @param lineLength
	 *            干扰线数目
	 * @return CreatVerificationCode
	 */
	public static VerificationCode getRichNewinstance(int height, int width,
			int charLength, int lineLength) {
		return new VerificationCode(height, width, charLength, lineLength);
	}

	/**
	 * 返回一个可以指定一切参数的类，当你获得这个对象实例时，该类内部已经产生了一张图片了
	 * 用户只需要通过getImage方法来获得图片，通过getText来获得验证内容即可，当需要将图片输出到页面时
	 * 只需要调用类中writeImage方法即可
	 * 
	 * @param data
	 *            验证码数据
	 * @param height
	 *            图片高度
	 * @param width
	 *            图片宽度
	 * @param charLength
	 *            验证码个数
	 * @param lineLength
	 *            干扰线数目
	 * @param fontNames
	 *            字体类型名称
	 * @param bgColor
	 *            背景颜色,字体颜色默认为背景颜色的相反色
	 * @return CreatVerificationCode
	 */
	public static VerificationCode getFullNewinstance(String data, int height,
			int width, int charLength, int lineLength, String[] fontNames,
			Color bgColor) {
		return new VerificationCode(data, height, width, charLength,
				lineLength, fontNames, bgColor);
	}

	/**
	 * 返回一个可以指定图片宽高的类，当你获得这个对象实例时，该类内部已经产生了一张图片了
	 * 用户只需要通过getImage方法来获得图片，通过getText来获得验证内容即可，当需要将图片输出到页面时
	 * 只需要调用类中writeImage方法即可
	 * 
	 * @param height
	 *            图片高度
	 * @param width
	 *            图片宽度
	 * @return CreatVerificationCode
	 */
	public static VerificationCode getSimpleNewinstance(int w, int h) {
		return new VerificationCode();
	}

	public BufferedImage getImage() {
		return image;
	}

	public String getText() {
		return text;
	}

	// 创建一张底图
	private BufferedImage creatImage() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(this.bgColor);
		g.fillRect(0, 0, width, height);
		return image;
	}

	// 生成验证码
	private char randomChar() {
		int n = r.nextInt(data.length());
		return data.charAt(n);
	}

	// 画图文字
	private void drawWord(char word, float x, float y) {
		Graphics2D g = image.createGraphics();
		g.setFont(randomFont());
		g.setColor(randomColor());
		g.drawString(word + "", x, y);
	}

	// 画出干扰线
	private void drawLine() {
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(255 - bgColor.getRed(), 255 - bgColor.getGreen(),
				255 - bgColor.getBlue()));

		for (int i = 0; i < lineLength; i++) {
			x1 = r.nextInt(width);
			y1 = r.nextInt(height);
			x2 = r.nextInt(width);
			y2 = r.nextInt(height);
			if (x1 == x2 && y1 == y2) {
				continue;
			}
			g.drawLine(x1, y1, x2, y2);
		}
	}

	// 随机颜色
	private Color randomColor() {
		int red = r.nextInt(150);
		int green = r.nextInt(150);
		int blue = r.nextInt(150);
		return new Color(red, green, blue);
	}

	// 随机字体
	private Font randomFont() {
		int index = r.nextInt(fontNames.length);
		String fontName = fontNames[index];
		int style = r.nextInt(4);
		int size = r.nextInt(width / 20) + width / (charLength - 1);
		return new Font(fontName, style, size);
	}

	// 将图片写出去
	public void writeImgae(OutputStream output) throws IOException {
		ImageIO.write(image, "JPEG", output);
	}

	// 将图片写出去
	public void writeImgae(BufferedImage image, OutputStream output)
			throws IOException {
		ImageIO.write(image, "JPEG", output);
	}
}
