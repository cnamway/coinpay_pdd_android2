package com.spark.modulebase.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 图片处理工具类
 * 基本理论：
 * 图片所占内存大小：图片长 x 图片宽 x 一个像素点占用的字节数组
 * 图片压缩格式：
 * Bitmap.Config.ARGB_8888  一个像素4个字节
 * Bitmap.Config.ARGB_565 一个像素2个字节
 **/

public class BitmapUtils {

    //压缩到1000k以下
    public static final int p_1000 = 1000;
    //压缩到300k以下
    public static final int p_300 = 300;


    /**
     * 采样率压缩：
     * 按照指定 宽高压缩图片
     * 默认 780 * 460
     *
     * @param is
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static Bitmap loadBitmap(InputStream is, int width, int height) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 5];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            os.write(buffer, 0, length);
            os.flush();
        }
        byte[] bytes = os.toByteArray();
        is.close();
        os.close();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        int w = opts.outWidth / width;
        int h = opts.outHeight / height;
        int scale = w > h ? w : h;
        if (scale < 1) {
            scale = 1;
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

    /**
     * 将指定bitmap对象存到文件中
     *
     * @param quality 质量压缩比率 取值 0--100 有些无损格式如png将忽略此设置不被压缩
     */
    public static void saveBitmapToFile(Bitmap bitmap, File file, int quality) throws IOException {
        if (bitmap == null) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, fos);
        fos.close();
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0) {
            width = 100;
        }
        if (height <= 0) {
            height = 100;
        }
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    /**
     * 获取压缩高度
     *
     * @param bitmap
     * @param w
     * @return
     */
    public static int zoomBitmapSize(Bitmap bitmap, int w) {
        int h = p_300;
        if (bitmap == null) {
            return h;
        }
        int width = bitmap.getWidth();//1000
        int height = bitmap.getHeight();//1000
        if (width <= 0) {
            width = p_300;
        }
        if (height <= 0) {
            height = p_300;
        }
        float scaleWidth = ((float) w / width);//0.1
        h = (int) (height * scaleWidth);
        LogUtils.e("图片压缩w=" + w + ",h=" + h);
        return h;
    }

    /**
     * 转换为base64格式
     */
    public static String imgToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imgBytes = out.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.NO_WRAP);//Android 类库
    }

    /**
     * 保存图片
     */
    public static File save(Bitmap saveBitmap, String dirName) {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        File folderFile = new File(dirName);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(dirName + fileName + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建文件输出流对象用来向文件中写入数据
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //将bitmap存储为jpg格式的图片
        saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        //刷新文件流
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将指定bitmap对象存到文件中
     *
     * @param quality 质量压缩比率 取值 0--100 有些无损格式如png将忽略此设置不被压缩
     */
    public static void saveBitmapToFile2(Bitmap bitmap, File file, int quality) throws IOException {
        if (bitmap == null) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        //创建文件输出流对象用来向文件中写入数据
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //将bitmap存储为jpg格式的图片
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        //刷新文件流
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 说明：压缩本地图片路径
     *
     * @param srcPath
     * @param rate    压缩到多少k
     */
    public static void compress(String srcPath, int rate) {
        float hh = 1920;
        float ww = 1080;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        //大于rate k就进行压缩
        while (baos.toByteArray().length > rate * 1024) {
            baos.reset();
            quality -= 2;// 每次都减少2
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        try {
            baos.writeTo(new FileOutputStream(srcPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
