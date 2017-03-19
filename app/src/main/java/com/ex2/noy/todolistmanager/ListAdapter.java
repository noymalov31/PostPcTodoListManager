package com.ex2.noy.todolistmanager;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noy on 18/03/2017.
 */

public class ListAdapter extends ArrayAdapter<String> {

    private TextView myText;
    private List<String> listOfItems;
    private LinearLayout layout;
    private int counter = 0;

    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.listOfItems = objects;
    }

    public void add(String item) {
        super.add( "* " + item);
        counter += 1;
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
        if (v != null){
            v.setBackgroundColor(position % 2 == 0 ? Color.RED : Color.BLUE);
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
