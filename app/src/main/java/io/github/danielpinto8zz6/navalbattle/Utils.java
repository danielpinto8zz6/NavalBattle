package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

public class Utils {
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 85, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int generateRandomNumber(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static String convertTwoDimensionalIntArrayToString(int[][] i){
        ByteArrayOutputStream bo = null;
        ObjectOutputStream so = null;
        Base64OutputStream b64 = null;
        try {
            bo = new ByteArrayOutputStream();
            b64 = new Base64OutputStream(bo, Base64.DEFAULT);
            so = new ObjectOutputStream(b64);
            so.writeObject(i);
            return bo.toString("UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (bo != null) { bo.close(); }
                if (b64 != null) { b64.close(); }
                if (so != null) { so.close(); }
            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
        return null;
    }

    public int[][] convertStringToTwoDimensionalIntArray(String s) {
        ByteArrayInputStream bi = null;
        ObjectInputStream si = null;
        Base64InputStream b64 = null;
        try {
            byte b[] = s.getBytes("UTF-8");
            bi = new ByteArrayInputStream(b);
            b64 = new Base64InputStream(bi, Base64.DEFAULT);
            si = new ObjectInputStream(b64);
            return (int[][]) si.readObject();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if (bi != null) { bi.close(); }
                if (b64 != null) { b64.close(); }
                if (si != null) { si.close(); }
            }catch (Exception ee){
                ee.printStackTrace();
            }

        }
        return null;
    }
}
