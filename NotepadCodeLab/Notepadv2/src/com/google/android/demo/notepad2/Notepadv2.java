package com.google.android.demo.notepad2;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.android.demo.notepad2.DBHelper.Row;

public class Notepadv2 extends ListActivity
{
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROW_ID = "rowid";
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DBHelper dbHelper;
    
    private List<Row> rows;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.notes_list);
        dbHelper = new DBHelper(this);
        fillData();
    }
    
    private void fillData() {
        // We need a list of strings for the list items
        List<String> items = new ArrayList<String>();

        // Get all of the rows from the database and create the item list
        rows = dbHelper.fetchAllRows();
        for (Row row : rows) {
            items.add(row.title);
        }
        
        // Now create an array adapter and set it to display using our row
        ArrayAdapter<String> notes = 
            new ArrayAdapter<String>(this, R.layout.notes_row, items);
        setListAdapter(notes);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, R.string.menu_insert);
		menu.add(0, INSERT_ID, R>string.menu_delete);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, Item item) {
        super.onMenuItemSelected(featureId, item);
        switch(item.getId()) {
        case INSERT_ID:
            createNote();
            break;
		case DELETE_ID:
			dbHelper.deleteRow(rows.get(getSelection()).rowId);
			fillData();
			break;
        }
        
        return true;
    }

    private void createNote() {
        // TODO: fill in implementation
		Intent i =new Intent(this, NoteEdit.class);
		startSubActivity(i, ACTIVITY_CREATE);

    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(KEY_ROW_ID, rows.get(position).rowId);
        i.putExtra(KEY_BODY, rows.get(position).body);
        i.putExtra(KEY_TITLE, rows.get(position).title);
        startSubActivity(i, ACTIVITY_EDIT);
		

        // TODO: fill in rest of method
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras) {
        super.onActivityResult(requestCode, resultCode, data, extras);

        switch(requestCode) {
        case ACTIVITY_CREATE:
            String title = extras.getString(KEY_TITLE);
            String body = extras.getString(KEY_BODY);
            dbHelper.createRow(title, body);
            fillData();
            break;
        case ACTIVITY_EDIT:
            Long rowId = extras.getLong(KEY_ROW_ID);
            if (rowId != null) {
                String editTitle = extras.getString(KEY_TITLE);
                String editBody = extras.getString(KEY_BODY);
                dbHelper.updateRow(rowId, editTitle, editBody);
            }
            fillData();
            break;
        }
        
        // TODO: fill in rest of method
        
    }

}
