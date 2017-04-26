package com.ex2.noy.todolistmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by noy on 18/03/2017.
 */

public class ListAdapter extends ArrayAdapter<String> {

    private TextView myText;
    private List<String> listOfItems;
    private LinearLayout layout;
    private Context context;


    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOfItems = objects;

    }

    public void add(String item, String Key) {
        super.add(item);
    }


    public int getCount(){
        return this.listOfItems.size();
    }

    public void deleteItem(int index){
        this.listOfItems.remove(index);
    }

    public String getItem(int index){

        return this.listOfItems.get(index);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null){
            LayoutInflater inflatter = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflatter.inflate(R.layout.one_item, parent, false);
        }
        layout = (LinearLayout) v.findViewById(R.id.item);
        String myString = getItem(position);
        myText = (TextView) v.findViewById(R.id.singleItem);
        myText.setText(myString);
        layout.setGravity(Gravity.LEFT);
        Collections.sort(this.listOfItems, new dateComparator());
        if (v != null){
            String today = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
            dateComparator dc = new dateComparator();
            if (dc.compare(today,myString) > 0){
                v.setBackgroundColor(Color.RED);
            }else{
                v.setBackgroundColor(Color.GREEN);
            }
        }
        return v;
    }

    public Bitmap decodeToBitmap (byte[] decodedBytes){
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String[] getValues() {
        String[] strArr = new String[this.listOfItems.size()];

        return (this.listOfItems.toArray(strArr));
    }
}

class dateComparator implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        String[] date_a = a.split(":");
        String[] date_parts_a = date_a[0].split("\\.");
        String[] date_b = b.split(":");
        String[] date_parts_b = date_b[0].split("\\.");

        if (date_parts_a[2].compareTo(date_parts_b[2]) != 0){
            return date_parts_a[2].compareTo(date_parts_b[2]);
        }
        else if  (Integer.parseInt(date_parts_a[1]) - Integer.parseInt(date_parts_b[1]) != 0){
            return  Integer.parseInt(date_parts_a[1]) - Integer.parseInt(date_parts_b[1]);
        }
        else{
            return  Integer.parseInt(date_parts_a[0]) - Integer.parseInt(date_parts_b[0]);
        }

    }
}
