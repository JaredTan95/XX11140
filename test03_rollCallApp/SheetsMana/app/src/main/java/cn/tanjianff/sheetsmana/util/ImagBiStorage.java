package cn.tanjianff.sheetsmana.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.ByteArrayOutputStream;

import cn.tanjianff.sheetsmana.R;

/**
 * Created by tanjian on 16/11/3.
 * 该类主要完成图片的Bitmap与byte[]二进制之间的转换,以此方便图片在SQLite中的存取
 */

public class ImagBiStorage {
    private static Context context;
    public ImagBiStorage(Context context){
        this.context=context;
    }
    //id为资源R中的id
    public Bitmap getBitmapFromDrawable(int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }

    //将Drawable转换成Bitmap
    public Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    //id为资源R中的id
    public byte[] Img2Byte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(id)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //获取存入数据库的图片（Bitmap）,需传入图片二进制流
    public Bitmap getBitmap(byte[] img){
        if(img==null){
            Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_icon)).getBitmap();
            byte[] bytes=new ImagBiStorage(context).Img2Byte(bitmap);
            img=bytes;
        }
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }

    public Drawable Bitmap2drawable(Bitmap bp) {
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        Bitmap bm = bp;
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
        return bd;
    }
}
