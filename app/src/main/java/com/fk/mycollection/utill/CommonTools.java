package com.fk.mycollection.utill;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
        bitmap = zoomImage(bitmap, 800, 480);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
//    String iconString2=new String(imagedata);
//    result = Base64.encodeToString(imagedata, Base64.DEFAULT);
            String iconString = Base64.encodeToString(imagedata, Base64.DEFAULT);
            return iconString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
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
     * bitmap转为base64
     *
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
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
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
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
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

    //1048576
    public static String getBase64(Context context, Uri uri, String filepath, int limitSize)
            throws FileNotFoundException, IOException {
        String mImgThString = "";
        if (uri != null) {
            Bitmap photoBig = BitmapAsset.getThumbnailByLimitSize(context, uri,
                    limitSize);
            int angle = 0;
            if (!TextUtils.isEmpty(filepath)) {
                angle = BitmapAsset.readPictureDegree(filepath);
            }
            photoBig = BitmapAsset.rotaingImageView(angle, photoBig);
            mImgThString = bitmapToBase64(photoBig);
            photoBig.recycle();
        }
        return mImgThString;
    }


    /**
     * 根据Uri的不同Scheme解析出在本机的路径
     *
     * @param context
     * @param uri
     * @return Uri的真实路径
     */
    @TargetApi(19)
    public static String formatUri(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    /**
     * 判断是否是车牌号
     */
    public static boolean isCarNo(String CarNum) {
        //匹配第一位汉字
        String str = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼甲乙丙己庚辛壬寅辰戍午未申";
        if (!(CarNum == null || CarNum.equals(""))) {
            String s1 = CarNum.substring(0, 1);//获取字符串的第一个字符
            if (str.contains(s1)) {
                String s2 = CarNum.substring(1, CarNum.length());
                //不包含I O i o的判断
                if (s2.contains("I") || s2.contains("i") || s2.contains("O") || s2.contains("o")) {
                    return false;
                } else {
                    if (!CarNum.matches("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$")) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }


    public static boolean isCarNo1(String carNo) {
        Pattern p = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(?:(?![A-Z]{4})[A-Z0-9]){4}[A-Z0-9挂学警港澳]{1}$");
        Matcher m = p.matcher(carNo);
        if (!m.matches()) {
            return false;
        }
        return true;

    }

    //手机号

    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    public static boolean isCarnumberNO(String carnumber) {
        String carnumRegex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";

        if (TextUtils.isEmpty(carnumber)) return false;
        else return carnumber.matches(carnumRegex);
    }

    public static String ArrayToString(List<String> list, String separator) {
        String result = "";
        for (String s : list) {
            result += s + ",";
        }
        result = result.length() <= 0 ? "" : result.substring(0, result.length() - 1);
        return result;
    }
    public static Drawable chageColor(Drawable drawable, int color){
        Drawable tintIcon= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(tintIcon,color);
        return tintIcon;
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
//    public static Map<Object, Object> sortMapByKey(Map<Object, Object> map) {
//        if (map == null || map.isEmpty()) {
//            return null;
//        }
////        Map<Object, Object> sortMap = new TreeMap<Object, Object>(new Comparator<String>(){
////            public int compare(String str1,String str2){
////                int i = str1.compareTo(str2);
////                return i;
////            }
////        });
////        sortMap.putAll(map);
//        return sortMap;
//    }

}


