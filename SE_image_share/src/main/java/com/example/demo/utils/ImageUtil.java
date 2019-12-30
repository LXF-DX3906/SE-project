package com.example.demo.utils;

import com.example.demo.utils.baiduUtils.Base64Util;
import com.example.demo.utils.baiduUtils.HttpUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * @program: SE_image_share
 * @description: 图片处理工具类
 * @author: Xuefei Lv
 * @create: 2019/12/30
 **/

public class ImageUtil {

    //对图片进行压缩
    public static byte[] compressFile(MultipartFile file) throws IOException {

        //获取图片文件名
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        //获取图片文件名后缀
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
        //通过bufferedImage可以获得图片尺寸
        BufferedImage bufferedImage =ImageIO.read(file.getInputStream());
        //将文件转为字节流
        byte[] imageBytes = file.getBytes();
        //创建输出流对象
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //对尺寸进行压缩
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();
        if(h > 4000 || w > 4000){
            if(w > h) {h = h * 4000 / w; w = 4000;}
            else {w = w * 4000 / h; h = 4000;}
            bufferedImage = Thumbnails.of(bufferedImage)
                    .size(w, h)
                    .asBufferedImage();
            ImageIO.write( bufferedImage, fileSuffix, outputStream);
            imageBytes = outputStream.toByteArray();
        }

        //对大小进行压缩
        if(imageBytes.length>4*1024*1024){
            double accuracy = (float)(4*1024*1024)/(float)imageBytes.length;
            bufferedImage = Thumbnails.of(bufferedImage)
                    .scale(accuracy)
                    .outputQuality(accuracy)
                    .asBufferedImage();
            outputStream = new ByteArrayOutputStream();
            ImageIO.write( bufferedImage, fileSuffix, outputStream);
            imageBytes = outputStream.toByteArray();
        }
        return imageBytes;
    }

    //将图片放入百度图像搜索库中
    public static String addImgToBaiDu(MultipartFile file,Integer pid) throws Exception {
        try {
            // 请求url
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/similar/add";
            //对图片进行压缩，转化为字节流
            byte[] imageBytes = ImageUtil.compressFile(file);

            String imgStr = Base64Util.encode(imageBytes);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "brief=" + pid + "&image=" + imgParam + "&tags=" + 1;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.50604b381a6660ae319ee412cfb3052e.2592000.1580220296.282335-18135170";
            return HttpUtil.post(url, accessToken, param);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //在百度图像搜索库中搜索图像
    public static String searchImgInBaiDu(MultipartFile file) throws Exception {
        try {
            // 请求url
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/similar/search";
            //对图片进行压缩，转化为字节流
            byte[] imageBytes = ImageUtil.compressFile(file);
            String imgStr = Base64Util.encode(imageBytes);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam + "&pn=" + 0 + "&rn=" + 20;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.50604b381a6660ae319ee412cfb3052e.2592000.1580220296.282335-18135170";
            return HttpUtil.post(url, accessToken, param);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
