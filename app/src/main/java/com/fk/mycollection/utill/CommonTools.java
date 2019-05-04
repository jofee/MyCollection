package com.fk.mycollection.utill;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.kobjects.base64.Base64;

public class CommonTools {

    // 最大显示时间长度
    private static final int TIME_LENGTH_MAX = 16;

    /**
     * 图片输入流
     */
    public static String getBitmapStream(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode;

            responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                String value = bitMapToString(bitmap);
                System.out.println("图片流");
                return value;

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param time
     * @return
     */
    public static String detailTimeToShow(String time) {
        if (!TextUtils.isEmpty(time)) {
            if (time.length() > TIME_LENGTH_MAX) {

                time = time.substring(0, TIME_LENGTH_MAX);
            }
        }
        return time;
    }

    /**
     * 处理图片，解决内存溢出的问题
     *
     * @param bitmap
     * @return
     */
    public Bitmap readBitmap(Bitmap bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // width，height设为原来的二分一
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        Bitmap btp = BitmapFactory.decodeStream(is, null, options);
        return btp;
    }

    /**
     * 让嵌套的gridview正常显示出来(num表示每行显示几个)
     */
    // // 显示list中嵌套的gridview
    public static void setGridViewHeight(GridView gv, int num, int height) {
        ListAdapter listAdapter = gv.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        // listItem.measure(0, 0);
        int rows = (listAdapter.getCount() - 1) / num + 1;
        totalHeight = height * rows;
        System.out.println("得到的长度" + totalHeight);
        // for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //
        // listAdapter.getCount()返回数据项的数目
        // View listItem = listAdapter.getView(i, null, gv);
        // listItem.measure(0, 0); // 计算子项View 的宽高
        // totalHeight += (listItem.getMeasuredHeight() - 1); //
        // 统计所有子项的总高度(减1是为了去掉分割线)
        // }

        ViewGroup.LayoutParams params = gv.getLayoutParams();
        params.height = totalHeight;
        // + (gv.getHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        gv.setLayoutParams(params);
    }

    /***
     * 解决内存溢出
     *
     * */
    public static Bitmap fitSizeImg(String path) {

        if (path == null || path.length() < 1)
            return null;
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        // if (file.length() < 20480) { // 0-20k
        // opts.inSampleSize = 1;
        // } else if (file.length() < 51200) { // 20-50k
        // opts.inSampleSize = 2;
        // } else if (file.length() < 307200) { // 50-300k
        // opts.inSampleSize = 4;
        // } else if (file.length() < 819200) { // 300-800k
        // opts.inSampleSize = 6;
        // } else if (file.length() < 1048576) { // 800-1024k
        // opts.inSampleSize = 8;
        // } else {
        // opts.inSampleSize = 10;
        // }

        if (file.length() < 307200) {
            opts.inSampleSize = 1;
        } else if (file.length() < 819200) {
            opts.inSampleSize = 2;
        } else {
            opts.inSampleSize = 4;
        }
        try {
            resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        } catch (OutOfMemoryError w) {
            opts.inSampleSize = 8;
            try {
                resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
            } catch (OutOfMemoryError o) {
                return null;
            }
        }
        return resizeBmp;
    }

    /***
     * 图片压缩递归算法 解决内存溢出
     *
     * */
    public static Bitmap fitSizeImg(Resources res, int drawableId, int inSampleSize) {
        if (drawableId == 0)
            return null;
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = inSampleSize;
        try {
            resizeBmp = BitmapFactory.decodeResource(res, drawableId, opts);
        } catch (OutOfMemoryError w) {
            if (inSampleSize >= 8) {
                return null;
            }
            fitSizeImg(res, drawableId, 2 * inSampleSize);
        }
        return resizeBmp;
    }

    /**
     * 将图片转换成Base64
     */
    public static String bitMapToString(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;

        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                int options = 100;
//        int options = 100;
                while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    baos.reset();//重置baos即清空baos
                    //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    //这里压缩options%，把压缩后的数据存放到baos中
                    options -= 10;//每次都减少10
                }
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();

                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage ：源图片资源
     * @param newWidth ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 将Base64字符串转换成图片
     */

    public static Bitmap stringtoBitmap(String string) {

        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(string, Base64.DEFAULT);
//
//      byte[] bitmapArray;
//      bitmapArray = Base64.decode(string);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
        // 　　// 将字符串转换成Bitmap类型

    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证email格式
     */
    public static boolean isEmailFormat(String line) {
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(line);
        return m.find();

    }

    public static int getVersionCode(Context context)// 获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取手机版本号
     *
     * @param context
     * @return String
     * @Title: getVersionName
     * @author renxh
     * @since 2015-10-22 V 1.0
     */
    public static String getVersionName(Context context)// 获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1";
        }
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if (input == null) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String getBase64(Context context, Uri uri)
            throws FileNotFoundException, IOException {
        Bitmap photoBig = getThumbnailByLimitSize(context, uri,
                1024 * 1024);
        photoBig = rotaingImageView(90, photoBig);
        String mImgThString = bitMapToString(photoBig);
        photoBig.recycle();
        return mImgThString;
    }

    public static String getBase64(Context context, Uri uri, String filepath)
            throws FileNotFoundException, IOException {
        Bitmap photoBig = getThumbnailByLimitSize(context, uri,
                2 * 1024 * 1024);
        int angle = readPictureDegree(filepath);
        photoBig = rotaingImageView(angle, photoBig);
        String mImgThString = bitMapToString(photoBig);
        photoBig.recycle();
        return mImgThString;
    }
    public static String getBase64(Context context, Uri uri, String filepath, int limitSize)
            throws FileNotFoundException, IOException {
        String mImgThString="";
        if(uri!=null) {
            Bitmap photoBig = getThumbnailByLimitSize(context, uri,
                    limitSize);
            int angle = 0;
            if (!TextUtils.isEmpty(filepath)) {
                angle = readPictureDegree(filepath);
            }
            photoBig = rotaingImageView(angle, photoBig);
            mImgThString= bitmapToBase64(photoBig);
            photoBig.recycle();
        }
        return mImgThString;
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 压缩图片
     *
     * @param context
     * @param uri
     * @param limitSize
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getThumbnailByLimitSize(Context context, Uri uri, int limitSize)
            throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, bmOptions);
        int originalSize = bmOptions.outWidth * bmOptions.outHeight;//
        int scale = 1;
        if (originalSize > limitSize && limitSize > 0) {
            scale = originalSize % limitSize == 0 ? originalSize / limitSize : originalSize /
                    limitSize + 1;
        }
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
        input.close();
        return bitmap;
    }

    /*
             * 旋转图片
             *
             * @param angle
             *
             * @param bitmap
             *
             * @return Bitmap
             */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //压缩图片尺寸
    public static Bitmap compressBySize(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) 300);
        int heightRatio = (int) Math.ceil(imgHeight / (float) 300);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        //设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(regExp);
    }

    //  compressImage    /**
//   * 通过uri获取图片并进行压缩
//   *
//   * @param uri
//   */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException,
            IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


}
