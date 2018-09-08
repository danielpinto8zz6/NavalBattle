package io.github.danielpinto8zz6.navalbattle.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.github.danielpinto8zz6.navalbattle.R;

public class BattleFieldAdapter extends BaseAdapter {
    private final Context context;
    private BattleField battleField;
    private final int imageDimension;

    public BattleFieldAdapter(Context c, BattleField battleField, int imageDimension) {
        context = c;
        this.battleField = battleField;
        this.imageDimension = imageDimension;
    }

    //---returns the number of images---
    public int getCount() {
        return 64;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        int x = position % 8;
        int y = (int) Math.ceil(position / 8);

        return battleField.get(x, y);
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int x = position % 8;
        int y = (int) Math.ceil(position / 8);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(imageDimension, imageDimension));

        viewHolder.imageView.setImageResource(battleField.get(x, y));

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}