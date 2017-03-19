package com.ex2.noy.todolistmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ListAdapter adapter;
    private  ArrayList<String> listItems ;
    private EditText newItem;
    private ListView list;
    static final String TEXT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newItem = (EditText) findViewById(R.id.edit);
        listItems = new ArrayList<String>();
        adapter = new ListAdapter(getApplicationContext(), R.layout.one_item, listItems);
        fab = (FloatingActionButton) findViewById(R.id.addFab);
        list = (ListView) findViewById(R.id.list);

        if (savedInstanceState != null) {
            String[] values = savedInstanceState.getStringArray("myKey");
            listItems = new ArrayList<String>(Arrays.asList(values));
            newItem.setText(listItems.toString());
            if (values != null) {
                adapter = new ListAdapter(getApplicationContext(), R.layout.one_item,listItems);
                adapter.notifyDataSetChanged();
            }
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addItem();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle(adapter.getItem(position));
                b.setMessage("Do you want to delete?");
                b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        adapter.deleteItem(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                b.show();

                return true;
            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addItem();
        return true;
    }

    private void addItem() {
        adapter.add(newItem.getText().toString());
        newItem.setText("");
        // Hide keyboard after send.
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        newItem.setText(savedInstanceState.getString(TEXT));
        Parcelable listViewState = savedInstanceState.getParcelable("list.state");
        list.onRestoreInstanceState(listViewState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TEXT, newItem.getText().toString());
        outState.putParcelable("list.state", list.onSaveInstanceState());
        String[] values = adapter.getValues();
        outState.putStringArray("myKey", values);

    }

}
