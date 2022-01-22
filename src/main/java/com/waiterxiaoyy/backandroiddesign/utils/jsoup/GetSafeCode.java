package com.waiterxiaoyy.backandroiddesign.utils.jsoup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetSafeCode {

    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码


    public String getSafeCode(byte[] bytes) throws IOException {
        ImgIdenfy imgIdenfy = new ImgIdenfy();
        //InputStream inputStream = new URL(url_safecode).openStream();

        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage sourceImg = ImageIO.read(inputStream);
        imgIdenfy.writeImage(sourceImg);
        int[][] imgArr = imgIdenfy.binaryImg(sourceImg); // 二值化
        imgIdenfy.removeByLine(imgArr); // 去除干扰先 引用传递
        int[][][] imgArrArr = imgIdenfy.imgCut(imgArr,
                new int[][]{new int[]{4, 13}, new int[]{14, 23}, new int[]{24, 33}, new int[]{34, 43}},
                new int[][]{new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}},
                4);
        return imgIdenfy.matchCode(imgArrArr);
    }
}
