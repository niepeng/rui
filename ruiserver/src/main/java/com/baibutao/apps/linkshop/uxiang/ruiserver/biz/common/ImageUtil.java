package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.StringUtil;

/**
 * @author niepeng
 */
public class ImageUtil {

	protected final static Logger log = LoggerFactory.getLogger(ImageUtil.class);

	public static final String JPG = ".jpg";

	public static byte[] getDataByUrl(String url) {
		try {
			if (StringUtil.isBlank(url)) {
				return null;
			}
			InputStream in = new URL(url).openStream();
			return inputStreamToByte(in);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void saveFile(byte[] bytes, String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return;
		}
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(file));
			outputStream.write(bytes);
		} catch (Exception e) {
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
			}
		}
	}

	public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	// max为限定图片的宽度，如果大于这个数字 就会被缩放，以宽度为基准。
	public static void zoomPicture(File inputFile, String outputPicName, double max) {
		double maxLimit = max;
		double ratio = 1.0;
		try {
			BufferedImage Bi = ImageIO.read(inputFile);
			if ((Bi.getWidth() > maxLimit)) {
				ratio = maxLimit / Bi.getWidth();
			}
			int widthdist = (int) Math.floor(Bi.getWidth() * ratio), heightdist = (int) Math.floor(Bi.getHeight() * ratio);
			BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(Bi.getScaledInstance(widthdist, heightdist, BufferedImage.SCALE_SMOOTH), 0, 0, null);
			File littleFile = new File(outputPicName);
			ImageIO.write(tag, "JPEG", littleFile);
		} catch (Exception ex) {
			log.error("zoomPicture error", ex);
		}
	}
	 /**
     *  缩放后裁剪图片方法
     * @param srcImageFile 源文件
     * @param x  x坐标
     * @param y  y坐标
     * @param destWidth 最终生成的图片宽
     * @param destHeight 最终生成的图片高
     * @param finalWidth  缩放宽度
     * @param finalHeight  缩放高度
     */
    public static byte[] abscut(String srcImageUrl, int x, int y, int destWidth,
                              int destHeight,int finalWidth,int finalHeight) {
        try {
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read( new URL(srcImageUrl)  );
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度

            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(finalWidth, finalHeight,Image.SCALE_DEFAULT);//获取缩放后的图片大小
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制截取后的图
                g.dispose(); 

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(tag, "JPEG", out);
                
                return out.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}