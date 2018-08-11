package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class BattleFieldAdapter extends BaseAdapter {
    private Context context;
    private BattleField battleField;

    public BattleFieldAdapter(Context c, BattleField battleField) {
        context = c;
        this.battleField = battleField;
    }

    //---returns the number of images---
    public int getCount() {
        return 64;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        int x = position % 8;
        int y = (int) Math.ceil(position / 8);

        return battleField.get(new Coordinates(x, y));
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int x = position % 8;
        int y = (int) Math.ceil(position / 8);

        Coordinates c = new Coordinates(x, y);
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

        imageView.setImageResource(battleField.get(c));

        return imageView;
    }
}