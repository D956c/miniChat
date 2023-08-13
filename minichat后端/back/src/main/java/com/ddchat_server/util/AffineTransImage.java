package com.ddchat_server.util;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 压缩图片
 */
public class AffineTransImage {
    //对图片进行压缩
    public static void minImage(String path,String source){
        try {
            File fi = new File(path+source); //大图文件
            String target = path + "min_"+source;
            System.out.println(source);
            System.out.println(target);
            File fo = new File(target); //将要转换出的小图文件
            int nw = 500;
			/*
			AffineTransform 类表示 2D 仿射变换，它执行从 2D 坐标到其他 2D
			坐标的线性映射，保留了线的“直线性”和“平行性”。可以使用一系
			列平移、缩放、翻转、旋转和剪切来构造仿射变换。
			*/
            AffineTransform transform = new AffineTransform();
            BufferedImage bis = ImageIO.read(fi); //读取图片
            int w = bis.getWidth();
            int h = bis.getHeight();
            //double scale = (double)w/h;
            int nh = (nw*h)/w ;
            double sx = (double)nw/w;
            double sy = (double)nh/h;
            transform.setToScale(sx,sy); //setToScale(double sx, double sy) 将此变换设置为缩放变换
			/*
			 * AffineTransformOp类使用仿射转换来执行从源图像或 Raster 中 2D 坐标到目标图像或
			 *  Raster 中 2D 坐标的线性映射。所使用的插值类型由构造方法通过
			 *  一个 RenderingHints 对象或通过此类中定义的整数插值类型之一来指定。
			如果在构造方法中指定了 RenderingHints 对象，则使用插值提示和呈现
			的质量提示为此操作设置插值类型。要求进行颜色转换时，可以使用颜色
			呈现提示和抖动提示。 注意，务必要满足以下约束：源图像与目标图像
			必须不同。 对于 Raster 对象，源图像中的 band 数必须等于目标图像中
			的 band 数。
			*/
            AffineTransformOp ato = new AffineTransformOp(transform,null);
            BufferedImage bid = new BufferedImage(nw,nh,BufferedImage.TYPE_3BYTE_BGR);
            /*
             * TYPE_3BYTE_BGR 表示一个具有 8 位 RGB 颜色分量的图像，
             * 对应于 Windows 风格的 BGR 颜色模型，具有用 3 字节存
             * 储的 Blue、Green 和 Red 三种颜色。
             */
            ato.filter(bis,bid);
            ImageIO.write(bid,"jpeg",fo);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
