package io.github.danielpinto8zz6.navalbattle;

import android.content.ClipData;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
    public Integer[] mThumbIds = {
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white,
            R.color.white, R.color.white, R.color.white, R.color.white
    };

    public ImageAdapter(Context c) {
        context = c;
    }

    //---returns the number of images---
    public int getCount() {
        return mThumbIds.length;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {

            WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);

            int bordersSize = Utils.convertDpToPixel(32);
            int actionbarSize = Utils.convertDpToPixel(56);

            int height = displayMetrics.heightPixels - bordersSize - actionbarSize;
            int width = displayMetrics.widthPixels - bordersSize;

            imageView = new ImageView(context);

            if (width < height)
                // we're in potrait mode
                imageView.setLayoutParams(new GridView.LayoutParams((height / 2) / 8, (height / 2) / 8));
            else
                imageView.setLayoutParams(new GridView.LayoutParams(height / 8, height / 8));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(0, 0, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        imageView.setTag(position);

        return imageView;
    }

    public void changeResource(int position, int resource) {
        if (position < 0 || position > mThumbIds.length) {
            return;
        }
        mThumbIds[position] = resource;
        notifyDataSetChanged(); // refresh the gridview
    }

}