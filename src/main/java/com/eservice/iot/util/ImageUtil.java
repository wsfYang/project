package com.eservice.iot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @program: yttps_shzx
 * @description: 提供图片的操作
 * @author: Mr.Zhang
 * @create: 2019-05-30 11:15
 **/
public class ImageUtil {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 图片转化成base64字符串
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile)
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            logger.error("image not find : "+imgFile);
            return null;
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        String imageBase64 = encoder.encode(data);
        if (imageBase64 != null&&!"".equals(imageBase64)) {
            return imageBase64.replaceAll("[\\s*\t\n\r]", "");
        }
        return null;
    }

    /**
     * base64字符串转化成图片
     * @param base64str
     * @param savePath
     * @return
     */
    public static boolean generateImage(String base64str,String savePath)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) {
            //图像数据为空
            return false;
        }
        //开始解码
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            //解码完成
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //开始生成图片生成jpeg图片
            OutputStream out = new FileOutputStream(savePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    /**
     * 图片转化成base64字符串
     * @param in
     * @return
     */
    public static String getImageStr(InputStream in)
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        //读取图片字节数组
        try
        {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            logger.error("image not find  ");
            return null;
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        String imageBase64 = encoder.encode(data);
        if (imageBase64 != null&&!"".equals(imageBase64)) {
            return imageBase64.replaceAll("[\\s*\t\n\r]", "");
        }
        return null;
    }


}
