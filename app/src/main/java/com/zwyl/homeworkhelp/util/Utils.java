package com.zwyl.homeworkhelp.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;

import com.zwyl.homeworkhelp.R;
import com.zwyl.homeworkhelp.base.ComFlag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    public static String getsubjectType(int selectId) {
        switch(selectId) {
            case ComFlag.NumFlag.EXERCISE_RADIO:
                return "单选题";
            case ComFlag.NumFlag.EXERCISE_MULTIPLE:
                return "多选题";
            case ComFlag.NumFlag.EXERCISE_JUDGE:
                return "判断题";
            case ComFlag.NumFlag.EXERCISE_GAP:
                return "填空题";
            case ComFlag.NumFlag.EXERCISE_COUNT:
                return "计算题";
            case ComFlag.NumFlag.EXERCISE_SHORT:
                return "简答题";

        }
        return "";
    }

    public static String getABC(int selectId) {
        switch(selectId) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "C";
        }
        return "";
    }

    public static String bitmapToString(Bitmap bitmap) {
        //将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 保存相机的图片
     **/
    public static String saveCameraImage(Bitmap bmp, String pictureName) {
        // 检查sd card是否存在
        if(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i("错误信息", "sd card is not avaiable/writeable right now.");
        }
        // 为图片命名啊
        // String name = new DateFormat().format("yyyymmdd", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        // 保存文件
        FileOutputStream fos = null;
        File file = new File("/mnt/sdcard/test/");
        file.mkdirs();// 创建文件夹
        String fileName = "/mnt/sdcard/test/" + pictureName + ".jpg";// 保存路径

        try {// 写入SD card
            fos = new FileOutputStream(fileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return file.getAbsolutePath() + "/" + pictureName + ".jpg";
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch(IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }// 显示图片
        // ((ImageView) findViewById(R.id.show_image)).setImageBitmap(bmp);
        return null;
    }


    /**
     * 文字转图片
     */
    public static Bitmap textAsBitmap(String text, float textSize) {


        TextPaint textPaint = new TextPaint();

        // textPaint.setARGB(0x31, 0x31, 0x31, 0);
        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(textSize);

        StaticLayout layout = new StaticLayout(text, textPaint, 450, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20, layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        Log.d("textAsBitmap", String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }
}
