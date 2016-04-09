/**
 * 学科网
 */
package com.xkw.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.web.multipart.MultipartFile;

/**
 * 同步或者异步抓取图片、缩图、加水印、存为JPG文件,运行main方法可以看到图形界面的演示，例子请见asMain方法.
 * @version   1.0.1
 * @author   bamboo
 */
public class GetPicManager {

    private static BufferedImage MASK_IMG;
    private static int MAX_PIC_SIZE = 1 * 1024 * 1024;
    private static ExecutorService FIXED_THREAD_POOL_EXECUTOR_SERVICE;

    /**
     *功能的简述. 对本类的参数进行配置,包括最大并发线程数目/超时等待时间/重试次数/水印等
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param maskPath 水印文件的位置
     * @param maxPicSize 原始图片最大文件尺寸,小于等于0使用缺省参数1M
     *  @param nThreads 最大并发线程数目
     * @return
     *注意：
     */
    public static void configManager(String maskPath, int maxPicSize, int nThreads) throws IOException {
    	  BufferedImage tmpImg = ImageIO.read(new File(maskPath));

          int w = tmpImg.getWidth(null);
          int h = tmpImg.getHeight(null);
          MASK_IMG = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
          Graphics2D g = MASK_IMG.createGraphics();
          g.drawImage(tmpImg, 0, 0, null);

          if (maxPicSize > 0) {
              GetPicManager.MAX_PIC_SIZE = maxPicSize;
          }

        if (nThreads <= 0) {
            nThreads = 10;
        }
        FIXED_THREAD_POOL_EXECUTOR_SERVICE = Executors.newFixedThreadPool(nThreads);
    }

    /**
     *功能的简述. 抓取一张图片(调用HTTPCLIENT,同步方法)
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param picUrl  图片的URL
     * @param refererUrl http请求的refererUrl,为空经常被对方的防盗链机制阻止抓取,不同网站防盗链机制对此参数要求不同,一般传入picUrl可以对付大部分网站
     *  @param sockTimeOutMillisecond sock超时毫秒数 小于等于0使用缺省参数 60秒
     *  @param retryCount 最大重试次数 小于等于0使用缺省参数 3次
     * @return   Object[],Object[0]为对方服务器返回的contentType字符串,
     * Object[1]为byte[],是对方服务器返回的二进制内容.
     *注意：
     */
    public static Object[] getPic(String picUrl, String refererUrl, int sockTimeOutMillisecond, int retryCount) throws IOException {
        HttpClient client = new HttpClient();
        if (sockTimeOutMillisecond <= 0) {
            sockTimeOutMillisecond = 60000;
        }
        if (retryCount <= 0) {
            retryCount = 3;
        }

        GetMethod method = new GetMethod(picUrl);
        method.addRequestHeader("Referer", refererUrl);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(retryCount, true));
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, new Integer(sockTimeOutMillisecond));

        int statusCode = client.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {
            throw new IOException("http state is NOT SC_OK , url : " + picUrl + " state : " + method.getStatusLine());
        }

        byte[] responseBody = method.getResponseBody(MAX_PIC_SIZE);
        //Content-Type
        String contentType = method.getResponseHeader("Content-Type").getValue();
        method.releaseConnection();

        return new Object[]{contentType, responseBody};
    }

    /**
     *功能的简述. 抓取图片,此方法在调用后立即返回(此时任务在后台开始执行).
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param picUrl  图片的URL
     * @param refererUrl http请求的refererUrl,为空经常被对方的防盗链机制阻止抓取,不同网站防盗链机制对此参数要求不同,一般传入picUrl可以对付大部分网站
     *  @param sockTimeOutMillisecond sock超时毫秒数 小于等于0使用缺省参数 60秒
     *  @param retryCount 最大重试次数 小于等于0使用缺省参数 3次
     * @param destPics 输出图片的描述(大小/存储位置/水印等)
     * @return   Future对象.
     *注意：
     */
    public static Future asynchronizedDownloadPic(
            final String picUrl, final String refererUrl, final int sockTimeOutMillisecond, final int retryCount, final PicDesc[] destPics) {
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    synchronizedDownloadPic(picUrl, refererUrl, sockTimeOutMillisecond, retryCount, destPics);
                } catch (Exception e) {
                    Logger.getLogger(GetPicManager.class.getName()).log(Level.SEVERE, "下载图片失败 picUrl: " + picUrl, e);
                }
            }
        };

        return FIXED_THREAD_POOL_EXECUTOR_SERVICE.submit(runnable);
    }

    /**
     *功能的简述. 抓取并缩放图片，加水印，加滤镜效果，存储为文件,为同步方法,此方法在调用后等待执行完毕后返回.
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param picUrl  图片的URL
     * @param refererUrl http请求的refererUrl,为空经常被对方的防盗链机制阻止抓取,不同网站防盗链机制对此参数要求不同,一般传入picUrl可以对付大部分网站
     *  @param sockTimeOutMillisecond sock超时毫秒数 小于等于0使用缺省参数 60秒
     *  @param retryCount 最大重试次数 小于等于0使用缺省参数 3次
     * @param destPics 输出图片的描述(大小/存储位置/水印等)
     * @return
     *注意：可能会抛出运行时异常
     */
    public static void synchronizedDownloadPic(
            String picUrl, String refererUrl, int sockTimeOutMillisecond, int retryCount, PicDesc[] destPics)
            throws Exception {
        Object[] pic = getPic(picUrl, refererUrl, sockTimeOutMillisecond, retryCount);
        byte[] picBytes = (byte[]) pic[1];
        synchronizedResizePic((String) pic[0], picBytes, destPics);
    }

    /**
     *功能的简述. 缩放图片，加水印，加滤镜效果，存储为文件,为同步方法,此方法在调用后等待执行完毕后返回.
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param inputFile  本地图片文件
     * @param destPics 输出图片的描述(大小/存储位置/水印等)
     * @return
     *注意：可能会抛出运行时异常
     */
    public static void synchronizedResizePic(File inputFile, PicDesc[] destPics)
            throws Exception {
        if (inputFile == null) {
            throw new NullPointerException();
        }
        FileInputStream fis = new FileInputStream(inputFile);
        int len = (int) inputFile.length();
        byte[] bytea = new byte[len];
        int readedLen = fis.read(bytea);
        if (readedLen != len) {
            throw new IOException();
        }
        synchronizedResizePic(inputFile.getCanonicalPath(), bytea, destPics);
        fis.close();
    }

    /**
     *功能的简述. 缩放图片，加水印，加滤镜效果，存储为文件,为同步方法,此方法在调用后等待执行完毕后返回.
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param inputUrlOrFileNameOrMimeType  传入图片的URL或者MIME类型用以判断图片文件格式
     * @param picBytes 图片文件的二进制内容
     * @param destPics 输出图片的描述(大小/存储位置/水印等)
     * @return
     *注意：可能会抛出运行时异常
     */
    public static void synchronizedResizePic(String inputUrlOrFileNameOrMimeType, byte[] picBytes, PicDesc[] destPics)
            throws Exception {
        ByteArrayInputStream imgBais = new ByteArrayInputStream(picBytes);
        // BufferedImage srcImg = ImageIO.read(imgBais); //碰到不能被加载的图片在此处抛出异常
        ImageInputStream iis = ImageIO.createImageInputStream(imgBais);
        Iterator iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            throw new Exception("not find image readers for :" + inputUrlOrFileNameOrMimeType);
        }
        ImageReader reader = (ImageReader) iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(iis, false, true);
        BufferedImage srcImg = reader.read(0, param);
        int numImages = reader.getNumImages(true);
        System.out.println("getNumImages:" + numImages);
        reader.dispose();
        iis.close();

        int srcWidth = srcImg.getWidth();
        int srcHeight = srcImg.getHeight();
        inputUrlOrFileNameOrMimeType = inputUrlOrFileNameOrMimeType.toLowerCase();
//        boolean inputPicIsGif = inputUrlOrFileNameOrMimeType.endsWith("gif");

        for (int i = 0; i < destPics.length; i++) {
            int destW, destH;
            destW = srcWidth;
            destH = srcHeight;

//            if (((srcWidth > destPics[i].getMaxOutputWidth()) || (srcHeight > destPics[i].getMaxOutputHeight()))
//                    && !(inputPicIsGif && (numImages > 1) && (destPics[i].getOutputFormat() == PictureOutputFormat.GIF_OUTPUT))) {
//                //需要使用PicDesc中设定的尺寸 (尺寸超限，并且不是动画GIF)
//                double scale = (srcWidth / ((double) destPics[i].getMaxOutputWidth())) > (srcHeight / ((double) destPics[i].getMaxOutputHeight())) ? (srcWidth / ((double) destPics[i].getMaxOutputWidth())) : (srcHeight / ((double) destPics[i].getMaxOutputHeight()));
//                destW = (int) (srcWidth / scale);
//                destH = (int) (srcHeight / scale);
//            } else {
//                //不需要缩图
//                if ((destPics[i].getPicMask() == null)
//                        || ((srcWidth < destPics[i].getPicMask().getNoMaskMaxWidth()) && (srcHeight < destPics[i].getPicMask().getNoMaskMaxHeight()))
//                        || (inputPicIsGif && (numImages > 1))) {
//                    //不需要打水印
//
//                    if (((destPics[i].getOutputFormat() == PictureOutputFormat.GIF_OUTPUT) && (inputUrlOrFileNameOrMimeType.endsWith("gif")))
//                            || ((destPics[i].getOutputFormat() == PictureOutputFormat.JPEG_OUTPUT) && (inputUrlOrFileNameOrMimeType.endsWith("jpg")))
//                            || ((destPics[i].getOutputFormat() == PictureOutputFormat.JPEG_OUTPUT) && (inputUrlOrFileNameOrMimeType.endsWith("jpeg")))) {
//                        //输入输出文件类型相同,GIF或者JPG
//                        //直接输出文件
//                        String fileSuffixName = destPics[i].getOutputFormat() == PictureOutputFormat.GIF_OUTPUT ? "gif" : "jpg";
//                        FileOutputStream fos = new FileOutputStream((destPics[i].isAddSuffix() ? destPics[i].getPath() + fileSuffixName : destPics[i].getPath()));
//                        fos.write(picBytes);
//                        fos.close();
//
//                        continue;
//                    }
//
//                }
//            }


//            InFocusPhoto ifp = new InFocusPhoto();//调用图象缩放方法
//            BufferedImage destImg = ifp.imageZoomOut(srcImg, destW, destH);


            if (destPics[i].getPicMask() != null) {
            	srcImg = doMask(srcImg, destPics[i]);
            }

            writeImg(srcImg, destPics[i]);
        }
    }

    //加水印
    private static BufferedImage doMask(BufferedImage img, PicDesc picDesc) {
        int imgW = img.getWidth();
        int imgH = img.getHeight();
        if ((imgW < picDesc.getPicMask().getNoMaskMaxWidth()) || (imgH < picDesc.getPicMask().getNoMaskMaxHeight())) {
            return img; //太小了不加水印
        }

        float[] scales = {1f, 1f, 1f, picDesc.getPicMask().getAlpha()};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);
        PicMask picMask = picDesc.getPicMask();

        BufferedImage maskImg = MASK_IMG;    //缺省不缩放水印图像
        int maskW = MASK_IMG.getWidth();
        int maskH = MASK_IMG.getHeight();

        //如果有必要，缩放水印图片
//        if (picMask.getMaskWidthScale() != null) {
//            float maskWidthScale = picMask.getMaskWidthScale();
//            if ((maskWidthScale > 0.1) && (maskWidthScale < 1)) {
//                //按宽度比例调整水印图像
//                float maskScale = maskH / (float) maskW;
//                maskW = (int) (imgW * maskWidthScale);
//                //maskH = (int) (imgH * maskWidthScale);
//                maskH = (int) (maskW * maskScale);
//                maskImg = new BufferedImage(maskW, maskH, BufferedImage.TYPE_INT_ARGB);
//                maskImg.createGraphics().drawImage(MASK_IMG, 0, 0, maskW, maskH, null);
//            }
//        }

        int startX = 0, startY = 0;//水印左上角在图片上的坐标
        switch (picMask.getMaskPosition()) {
            case TOP_LEFT:
                startX = 0;
                startY = 0;
                break;
            case TOP_RIGHT:
                startX = imgW - maskW;
                startY = 0;
                break;
            case BOTTOM_LEFT:
                startX = 0;
                startY = imgH - maskH;
                break;
            case BOTTOM_RIGHT:
                startX = imgW - maskW;
                startY = imgH - maskH;
                break;
            default://CENTER
                startX = (imgW - maskW) / 2;
                startY = (imgH - maskH) / 2;
                break;
        }

        img.createGraphics().drawImage(maskImg, rop, startX, startY);

        return img;
    }

    /**
     *功能的简述. 根据文件名或者URL或者MIME类型推测图片类型是否是GIF图片缩图打水印规则不同.
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param urlOrFileNameOrMime  文件名或者URL或者MIME类型
     * @return  图片类型
     *注意：
     */
    public static PictureOutputFormat getPictureOutputFormat(String urlOrFileNameOrMime) {
        if (urlOrFileNameOrMime.toLowerCase().endsWith("gif")) {
            return PictureOutputFormat.GIF_OUTPUT;
        } else {
            return PictureOutputFormat.JPEG_OUTPUT;
        }
    }

    private static void writeImg(BufferedImage destImg, PicDesc destPic) throws IOException {
        ImageWriter writer = null;
        String fileSuffixName = destPic.getOutputFormat() == PictureOutputFormat.GIF_OUTPUT ? "gif" : "jpg";
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(fileSuffixName);
        if (iter.hasNext()) {
            writer = iter.next();
        } else {
            throw new IOException("no ImageWriter for " + fileSuffixName);
        }

        ImageOutputStream ios = ImageIO.createImageOutputStream(new File(destPic.isAddSuffix() ? destPic.getPath() + fileSuffixName : destPic.getPath()));
        writer.setOutput(ios);


        ImageWriteParam iwparam = writer.getDefaultWriteParam();
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        if (destPic.getOutputFormat() == PictureOutputFormat.GIF_OUTPUT) {
            //GIF
            iwparam.setCompressionType("lzw");
            iwparam.setCompressionQuality(destPic.getQuality());
        } else {
            //JPEG
            iwparam.setCompressionType("JPEG");
            iwparam.setCompressionQuality(destPic.getQuality());
        }

        writer.write(null, new IIOImage(destImg, null, null), iwparam);

        ios.flush();
        writer.dispose();
        ios.close();
    }

    /**
     *表示水印位置的枚举类型,位置包括左上/右上/左下/右下/中间
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     */
    public static enum MaskPosition {

        /**
         *表示水印位于图片的左上
         */
        TOP_LEFT,
        /**
         *表示水印位于图片的右上
         */
        TOP_RIGHT,
        /**
         *表示水印位于图片的左下
         */
        BOTTOM_LEFT,
        /**
         *表示水印位于图片的右下
         */
        BOTTOM_RIGHT,
        /**
         *表示水印位于图片的中间
         */
        CENTER
    }

    /**
     *表示输出图片类型的枚举类型
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     */
    public static enum PictureOutputFormat {
        //AUTO_SELECT, //输入GIF输出GIF，其它一律输出JPG
/**
     *表示JPEG输出图片类型
 */
        JPEG_OUTPUT,
        /**
     *表示GIF输出图片类型
 */
        GIF_OUTPUT
    }

    /**
     *配置输出图片的水印参数
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     */
    public static class PicMask {

        private MaskPosition maskPosition;
        private float alpha;
        private Float maskWidthScale; //空值表示使用原始水印大小
        private int noMaskMaxWidth;
        private int noMaskMaxHeight;

        /**
     *配置输出图片的水印参数
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param maskPosition  水印位置
         * @param alpha  水印的透明度
         * @param maskWidthScale  按原图比例加水印(即水印大小不超过原图尺寸乘以此数),NULL表示按水印图片的原始尺寸加水印
         * @param noMaskMaxWidth  图片宽度小于此数则不打水印
         * @param noMaskMaxHeight  图片高度小于此数则不打水印
     *注意：
     */
        public PicMask(MaskPosition maskPosition, float alpha, Float maskWidthScale, int noMaskMaxWidth, int noMaskMaxHeight) {
            this.maskPosition = maskPosition;
            this.alpha = alpha;
            this.maskWidthScale = maskWidthScale;
            this.noMaskMaxWidth = noMaskMaxWidth;
            this.noMaskMaxHeight = noMaskMaxHeight;
        }

        private int getNoMaskMaxHeight() {
            return noMaskMaxHeight;
        }

        private void setNoMaskMaxHeight(int noMaskMaxHeight) {
            this.noMaskMaxHeight = noMaskMaxHeight;
        }

        private int getNoMaskMaxWidth() {
            return noMaskMaxWidth;
        }

        private void setNoMaskMaxWidth(int noMaskMaxWidth) {
            this.noMaskMaxWidth = noMaskMaxWidth;
        }

        private MaskPosition getMaskPosition() {
            return maskPosition;
        }

        private void setMaskPosition(MaskPosition maskPosition) {
            this.maskPosition = maskPosition;
        }

        private float getAlpha() {
            return alpha;
        }

        private void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        private Float getMaskWidthScale() {
            return maskWidthScale;
        }

        private void setMaskWidthScale(Float maskWidthScale) {
            this.maskWidthScale = maskWidthScale;
        }
    }

    /**
     *配置输出图片的参数
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param maskPosition  水印位置
         * @param alpha  水印的透明度
         * @param maskWidthScale  按原图比例加水印(即水印大小不超过原图尺寸乘以此数),NULL表示按水印图片的原始尺寸加水印
         * @param noMaskMaxWidth  图片宽度小于此数则不打水印
         * @param noMaskMaxHeight  图片高度小于此数则不打水印
     *注意：
     */
    public static class PicDesc {

        private String path;
        private boolean addSuffix;
        private PictureOutputFormat outputFormat;
        //        private int width;    //小于等于0表示无缩放，使用原始尺寸
        //        private int height;    //小于等于0表示无缩放，使用原始尺寸
        private int maxOutputWidth; //最大输出图片宽度
        private int maxOutputHeight; //最大输出图片高度
        private float quality;    //保存质量
        private PicMask picMask;  //空值表示无水印

        /**
     *配置输出图片的参数
     * <br>修改历史：
     * <br>修改日期  修改者 BUG小功能修改申请单号
     * <br>
     * @param path  输出图片的路径(含文件名)
         * @param addSuffix  xxxxx
         * @param outputFormat  输出格式JPEG/GIF
         * @param maxOutputWidth  图片最大宽度
         * @param maxOutputHeight  图片最大高度
         * @param quality  图片输出的压缩参数
         * @param picMask  水印的配置
         *
     *注意：
     */
        public PicDesc(String path, boolean addSuffix, PictureOutputFormat outputFormat, int maxOutputWidth, int maxOutputHeight, float quality, PicMask picMask) {
            this.path = path;
            this.addSuffix = addSuffix;
            this.outputFormat = outputFormat;
            this.maxOutputWidth = maxOutputWidth;
            this.maxOutputHeight = maxOutputHeight;
            this.quality = quality;
            this.picMask = picMask;

        }

        private boolean isAddSuffix() {
            return addSuffix;
        }

        private void setAddSuffix(boolean addSuffix) {
            this.addSuffix = addSuffix;
        }

        private int getMaxOutputHeight() {
            return maxOutputHeight;
        }

        private void setMaxOutputHeight(int maxOutputHeight) {
            this.maxOutputHeight = maxOutputHeight;
        }

        private int getMaxOutputWidth() {
            return maxOutputWidth;
        }

        private void setMaxOutputWidth(int maxOutputWidth) {
            this.maxOutputWidth = maxOutputWidth;
        }

        private PictureOutputFormat getOutputFormat() {
            return outputFormat;
        }

        private void setOutputFormat(PictureOutputFormat outputFormat) {
            this.outputFormat = outputFormat;
        }

        private String getPath() {
            return path;
        }

        private void setPath(String path) {
            this.path = path;
        }

        private PicMask getPicMask() {
            return picMask;
        }

        private void setPicMask(PicMask picMask) {
            this.picMask = picMask;
        }

        private float getQuality() {
            return quality;
        }

        private void setQuality(float quality) {
            this.quality = quality;
        }
    }

    /**
     * 测试方法,显示SWING界面查看效果
     * @param urls 待抓取的图片URL
     * @throws Exception
     */
    private static void asMain(String[] urls) throws Exception {
        //缺省最大抓取1M大小图片，启用10个线程
        GetPicManager.configManager("f:\\logo-tag.png", -1, -1);

        //测试代码
//查看测试输出
        JFrame frame = new JFrame("看输出");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel1 = new javax.swing.JPanel();
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        PicDesc[][] picDescAA = new PicDesc[urls.length][];
        for (int x = 0; x < urls.length; x++) {

            //抓取后输出目标的描述
            PicDesc[] picDescA = {
                //new PicDesc("" + x + "x100.jpg", false, PictureOutputFormat.JPEG_OUTPUT, 8192, 8192, 1, null, null), //原图输出，不加水印，不使用滤镜
//                new PicDesc("" + x + "x80.jpg", false, PictureOutputFormat.JPEG_OUTPUT, 8192, 8192, 0.8f, null), //原图输出，不加水印，不使用滤镜
                //new PicDesc("" + x + "x100.gif", false, PictureOutputFormat.GIF_OUTPUT, 8192, 8192, 1, null, null), //原图输出，不加水印，不使用滤镜
                //new PicDesc("" + x + "x80.gif", false, PictureOutputFormat.GIF_OUTPUT, 8192, 8192, 0.8f, null, null), //原图输出，不加水印，不使用滤镜
                //new PicDesc("" + x + "x20.gif", false, PictureOutputFormat.GIF_OUTPUT, 8192, 8192, 0.2f, null, null), //原图输出，不加水印，不使用滤镜

//                new PicDesc("" + x + "x3.jpg", false, PictureOutputFormat.JPEG_OUTPUT, 200, 100, 1,
//                new PicMask(MaskPosition.BOTTOM_RIGHT , (float) 0.3, new Float(0.8), 60, 60)) //缩图，使用滤镜，加水印，使用80%原图尺寸水印，水印透明度0.3
                new PicDesc("" + x + "x3.jpg", false, PictureOutputFormat.JPEG_OUTPUT, 8192, 8192, 1,
                      new PicMask(MaskPosition.BOTTOM_RIGHT , (float) 0.6, new Float(0.2), 60, 60))
            };
            picDescAA[x] = picDescA;
            //抓取
            //调用异步方法将抓取任务加入线程池  ，  并且在返回的Future对象上调用get()方法死等抓取完成
            GetPicManager.asynchronizedDownloadPic(
                    urls[x],
                    urls[x], //http头部的referer如果不方便确定，使用图片的URL对大多数防盗链都能搞定
                    -1, //缺省SOCKETTIMEOUT 60000毫秒
                    -1, //缺省重试3次
                    picDescA).get();
        }

        Thread.sleep(3000);

        for (int x = 0; x < urls.length; x++) {
            PicDesc[] picDescA = picDescAA[x];
            //frame.getContentPane().setLayout(new javax.swing.BoxLayout(frame.getContentPane(), javax.swing.BoxLayout.Y_AXIS));
            for (int i = 0; i < picDescA.length; i++) {
//                new File(picDescA[i].getPath()).deleteOnExit();     //生产环境千万别加这行！！！ 测试环境在程序退出时候干掉图片文件
                JButton button = new JButton(new ImageIcon(picDescA[i].getPath()));
                jPanel1.add(button);
                //frame.getContentPane().add(button);
            }
            jScrollPane1.setViewportView(jPanel1);
            frame.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
            //frame.pack();
            frame.setBounds(20, 20, 800, 600);
            frame.setVisible(true);
        }
    }

    /**   
     * 给图片添加水印、可设置水印图片旋转角度   
     * @param iconPath 水印图片路径   
     * @param srcImgFile 源图片路径   
     * @param targerPath 目标图片路径   
     */
    public static void markImage(String iconPath, MultipartFile srcImgFile,     
            String targerPath) throws Exception {
    	
    	  BufferedImage tmpImg = ImageIO.read(new File(iconPath));

          int w = tmpImg.getWidth(null);
          int h = tmpImg.getHeight(null);
          MASK_IMG = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
          Graphics2D g = MASK_IMG.createGraphics();
          g.drawImage(tmpImg, 0, 0, null);

    	  PicDesc[] picDescA = {  new PicDesc(targerPath, false, PictureOutputFormat.JPEG_OUTPUT, 8192, 8192, 1,
                  new PicMask(MaskPosition.BOTTOM_RIGHT ,(float) 1, null, 60, 60))};
          
          synchronizedResizePic(srcImgFile.getOriginalFilename(),srcImgFile.getBytes(),picDescA);
    }
    public static void main(String[] args) throws Exception {
        String[] urls = {
            "http://image.club.china.com/3216067/2009/1/11/1231678381584.jpg",
            "http://image.club.china.com/3216067/2009/1/11/1231678381601.jpg"
        /*"http://image.club.china.com/3216067/2009/1/4/mid/1231072810553.jpg",
        "http://image.club.china.com/3216067/2009/1/4/1231072810703.jpg",
        "http://image.club.china.com/3216067/2008/8/19/1219161493922.jpg",
        "http://image.club.china.com/3216067/2008/8/19/1219161494329.jpg"*/
        };
        /*String[] urls = {
        "http://www.yejs.com.cn/Upload2005/2007-5-11/2007511172715919.gif",
        "http://face.imobile.com.cn/370324.gif",
        //            "http://img1.bbs.ws.126.net/20090301/zplt/st/stonepzh/417c3451639e473bbd7f481cae32e3f8.jpg",
        //            "http://img.qbar.qq.com/cgi-bin/img?uuid=__e05341b700ca47b5affea2f21a325eec",
        //            "http://i3.sinaimg.cn/dy/c/2009-02-26/U2123P1T1D17295470F21DT20090226203040.jpg",
        //            "http://i1.sinaimg.cn/dy/c/2009-02-26/U2123P1T1D17295470F23DT20090226203040.jpg",
        //            "http://i1.sinaimg.cn/dy/c/2009-02-26/U2123P1T1D17295470F1394DT20090226203040.jpg",
        //            "http://i1.sinaimg.cn/dy/c/p/2009-02-26/09363b92f4c66ac965cae619acf6996d.jpg",
        //            "http://i0.sinaimg.cn/dy/c/p/2009-02-26/U2107P1T1D17289207F23DT20090226043601.jpg",
        "http://i1.sinaimg.cn/dy/c/p/2009-02-26/U2107P1T1D17289207F1394DT20090226045051.jpg"
        };*/
        
  	  BufferedImage tmpImg = ImageIO.read(new File("f:\\tabImage.png"));

      int w = tmpImg.getWidth(null);
      int h = tmpImg.getHeight(null);
      MASK_IMG = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = MASK_IMG.createGraphics();
      g.drawImage(tmpImg, 0, 0, null);
      File file = new File("f:\\mxcpimages201501160322360933_info320X534.png");
//      FileOutputStream out = new FileOutputStream(file);
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream baos=new ByteArrayOutputStream();
	  int c;
	  byte buffer[] = new byte[1024];
	  while((c = in.read(buffer)) != -1){
		  
		  baos.write(buffer,0,c);
	  }
	  PicDesc[] picDescA = {  new PicDesc("f:\\test水印x3.jpg", false, PictureOutputFormat.JPEG_OUTPUT, 8192, 8192, 1,
              new PicMask(MaskPosition.BOTTOM_RIGHT , (float) 1, null, 60, 60))};
      
      synchronizedResizePic("destImg.png",baos.toByteArray(),picDescA);
//      if (maxPicSize > 0) {
//          GetPicManager.MAX_PIC_SIZE = maxPicSize;
//      }
//        asMain(urls);
    }
}
