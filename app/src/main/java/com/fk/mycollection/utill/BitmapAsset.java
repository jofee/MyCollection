package com.fk.mycollection.utill;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BitmapAsset {


    public static void loadImage(Context context, String url,
                                 ImageView imageView, int holderId, int errorId) {
        Glide.with(context).load(url).placeholder(holderId).dontAnimate()
                .error(errorId).into(imageView);
    }

    public static void loadCircleImage(final Context context, String url, final ImageView imageView, int id, int ids) {
        Glide.with(context).load(url).asBitmap().centerCrop().placeholder(id).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 清除内存中的缓存 必须在UI线程中调用
     *
     * @param context
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘中的缓存 必须在后台线程中调用，建议同时clearMemory()
     *
     * @param context
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 优先级加载图片
     *
     * @param url
     * @param context
     * @param imageView
     * @param priority  优先级 Priority.LOW/Priority.HIGH
     */
    public static void loadImageWithPriority(String url, Context context,
                                             ImageView imageView, Priority priority) {
        Glide.with(context).load(url).priority(priority).into(imageView);
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
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_NORMAL);
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

    public static String fileToBase64String(String filePath) {
        File photoFile = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(photoFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(10000);
            byte[] buffer = new byte[1000];
            while (fis.read(buffer) != -1) {
                baos.write(buffer);
            }
            baos.close();
            fis.close();
            return Arrays.toString(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        bmOptions.inSampleSize = 1;
        BitmapFactory.decodeStream(input, null, bmOptions);
        int width = bmOptions.outWidth;
        int height = bmOptions.outHeight;
        double originalSize = width * height;//
        if (originalSize > limitSize) {
            double scale = originalSize / limitSize;
            width /= scale;
            height /= scale;
        }
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
        input.close();

        Bitmap result = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return result;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns
                    .DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * ImageView设置优化内存使用后的Bitmap
     * 返回一个等同于ImageView宽高的bitmap
     *
     * @param view    ImageView
     * @param imgPath 图像路径
     */
    public static void imageViewSetPic(ImageView view, String imgPath) {
        // Get the dimensions of the View
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一

        BitmapFactory.decodeFile(imgPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, bmOptions);
        view.setImageBitmap(bitmap);
    }

    //Glide保存图片
    public static void savePicture(final Context context, final String fileName, String url) {
        Glide.with(context).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    savaFileToSD(context, fileName, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //往SD卡写入文件的方法
    public static void savaFileToSD(Context context, String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            filename = storageDir.getAbsolutePath() + "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
            //通知系统相册
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(storageDir);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

}
