package com.google.android.demo.notepad1;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;

import com.google.android.demo.notepad1.DBHelper.Row;

public class Notepadv1 extends ListActivity
{
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    
    public static final int INSERT_ID = Menu.FIRST;
    
    private int noteNumber = 1;
    private DBHelper dbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.notepad_list);
        dbHelper = new DBHelper(this);
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(Item item) {
    	switch (item.getId()) {
    	case INSERT_ID:
    		createNote();
    		fillData();
    		break;
    	}
    	
        return super.onOptionsItemSelected(item);
    }
    
    private void createNote() {
    	String noteName = "Note " + noteNumber++;
    	dbHelper.createRow(noteName, "");
    	fillData();
    }
    
    private void fillData() {
        // We need a list of strings for the list items
        List<String> items = new ArrayList<String>();

        // Get all of the rows from the database and create the item list
        List<Row> rows = dbHelper.fetchAllRows();
        for (Row row : rows) {
            items.add(row.title);
        }
        
        // Now create an array adapter and set it to display using our row
        ArrayAdapter<String> notes = 
            new ArrayAdapter<String>(this, R.layout.notes_row, items);
        setListAdapter(notes);        
    }
}
