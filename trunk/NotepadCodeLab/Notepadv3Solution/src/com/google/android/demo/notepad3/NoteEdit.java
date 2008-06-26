package com.google.android.demo.notepad3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;;

public class NoteEdit extends Activity {

    private EditText titleText;
    private EditText bodyText;
    private Long rowId;
    private DBHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        dbHelper = new DBHelper(this);
        
        setContentView(R.layout.note_edit);
        
        titleText = (EditText) findViewById(R.id.title);
        bodyText = (EditText) findViewById(R.id.body);
        
        Button confirmButton = (Button) findViewById(R.id.confirm);
        
        rowId = icicle != null ? icicle.getLong(Notepadv3.KEY_ROW_ID) : null;
        if (rowId == null) {
            Bundle extras = getIntent().getExtras();            
            rowId = extras != null ? extras.getLong(Notepadv3.KEY_ROW_ID) : null;
        }
        
        populateFields();
        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
            
        });
    }

    private void populateFields() {
    	if (rowId != null) {
            DBHelper.Row row = dbHelper.fetchRow(rowId);
            if (row.rowId > -1) {
                titleText.setText(row.title);
                bodyText.setText(row.body);            
            }            
        }
    }

	@Override
	protected void onFreeze(Bundle outState) {
		super.onFreeze(outState);
        outState.putLong(Notepadv3.KEY_ROW_ID, rowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
        saveState();
        dbHelper.close();
        dbHelper = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
        if (dbHelper == null) {
            dbHelper = new DBHelper(this);
        }
        populateFields();
	}
    
	private void saveState() {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if (rowId == null) {
            dbHelper.createRow(title, body);
        } else {
            dbHelper.updateRow(rowId, title, body);
        }
    }
}
