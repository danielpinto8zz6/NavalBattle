package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import static android.content.Context.WINDOW_SERVICE;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ImageView imageView;

    public ImageAdapter(Context c) {
        context = c;
    }

    //---returns the number of images---
    public int getCount() {
        return Constants.boardSize;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);

            int bordersSize = Utils.convertDpToPixel(32);
            int actionbarSize = Utils.convertDpToPixel(56);

            int height = displayMetrics.heightPixels - bordersSize;
            int width = displayMetrics.widthPixels - bordersSize;

            imageView = new ImageView(context);

            if (width < height)
                imageView.setLayoutParams(new GridView.LayoutParams(width / 8, width / 8));
            else
                imageView.setLayoutParams(new GridView.LayoutParams((height - actionbarSize) / 8, (height - actionbarSize) / 8));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(0, 0, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.color.white);
        return imageView;
    }
}