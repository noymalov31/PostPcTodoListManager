package com.ex2.noy.todolistmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ListAdapter adapter;
    private  ArrayList<String> listItems ;
    private ListView list;
    static final String TEXT = "";
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("todo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listItems = new ArrayList<String>();
        adapter = new ListAdapter(getApplicationContext(), R.layout.one_item, listItems);
        fab = (FloatingActionButton) findViewById(R.id.addFab);
        list = (ListView) findViewById(R.id.list);
        final Calendar c = Calendar.getInstance();
        list.setAdapter(adapter);


        if (savedInstanceState != null) {
            String[] values = savedInstanceState.getStringArray("myKey");
            listItems = new ArrayList<String>(Arrays.asList(values));
            if (values != null) {
                adapter = new ListAdapter(getApplicationContext(), R.layout.one_item,listItems);
                adapter.notifyDataSetChanged();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(view.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText input = new EditText(view.getContext());
                input.setHint("What do you want to do?");
                layout.addView(input);
                final DatePicker dp = new DatePicker(view.getContext());
                layout.addView(dp);


                new AlertDialog.Builder(view.getContext())
                        .setTitle("Add Activity")
                        .setView(layout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String msg =  String.valueOf(dp.getDayOfMonth())+"."+String.valueOf(dp.getMonth()+1)+"." +String.valueOf(dp.getYear())+":";
                                msg += input.getText().toString();
                                dbRef.push().setValue(msg);

                                Toast.makeText(MainActivity.this, "activity added successfully ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "oh no..", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                String msg = adapter.getItem(position);
                String[] content = msg.split(":");
                b.setTitle(content[1]);
                b.setMessage("Do you want to delete this reminder?");

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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                final int myposition = position;
                final String msg = adapter.getItem(position);
                String[] content = msg.split(":");
                b.setTitle(content[1]);
                b.setMessage("At: " + content[0]);

                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                b.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        adapter.deleteItem(myposition);
                        adapter.notifyDataSetChanged();
                        dbRef.child(msg).removeValue();
                    }
                });


                final String[] content_parts = content[1].split(" ");
                if (Objects.equals(content_parts[0], "call") || Objects.equals(content_parts[0], "Call")){
                    b.setNeutralButton("call", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+content_parts[1]));
                            startActivity(intent);
                        }
                    });

                }

                b.show();


            }
        });


        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String msg = dataSnapshot.getValue(String.class);
                adapter.add(msg);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(MainActivity.this);
        input.setHint("What do you want to do?");
        layout.addView(input);
        final DatePicker dp = new DatePicker(MainActivity.this);
        layout.addView(dp);


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add Activity")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msg =  String.valueOf(dp.getDayOfMonth())+"."+String.valueOf(dp.getMonth()+1)+"." +String.valueOf(dp.getYear())+":";
                        msg += input.getText().toString();
                        adapter.add(msg);
                        Toast.makeText(MainActivity.this, "activity added successfully ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "oh no..", Toast.LENGTH_SHORT).show();
                    }
                }).show();

        return true;
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Parcelable listViewState = savedInstanceState.getParcelable("list.state");
        list.onRestoreInstanceState(listViewState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("list.state", list.onSaveInstanceState());
        String[] values = adapter.getValues();
        outState.putStringArray("myKey", values);

    }

}
