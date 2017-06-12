package com.aguspuryanto.laguazmp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AGUS on 11/06/2017.
 */

public class CustomList extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomList(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_layout, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView textViewName = (TextView)vi.findViewById(R.id.textViewName);
        String title = (String) data.get("title");
        textViewName.setText(title);

        TextView textViewLink = (TextView)vi.findViewById(R.id.textViewLink);
        String link = (String) data.get("link");
        textViewLink.setText(link);

        ImageView image=(ImageView)vi.findViewById(R.id.imageView);
        String image_url = (String) data.get("img");
        Picasso.with(mContext).load(image_url).into(image);
        return vi;
    }
}
