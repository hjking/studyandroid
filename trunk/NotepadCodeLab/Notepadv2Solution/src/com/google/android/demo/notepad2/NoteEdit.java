package com.google.android.demo.notepad2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {

	private EditText titleText;
	private EditText bodyText;
	private Long rowId;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        setContentView(R.layout.note_edit);
        
        titleText = (EditText) findViewById(R.id.title);
        bodyText = (EditText) findViewById(R.id.body);
       
        Button confirmButton = (Button) findViewById(R.id.confirm);
        
        rowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(Notepadv2.KEY_TITLE);
            String body = extras.getString(Notepadv2.KEY_BODY);
            rowId = extras.getLong(Notepadv2.KEY_ROW_ID);
           
            if (title != null) {
                titleText.setText(title);
            }
            if (body != null) {
                bodyText.setText(body);
            }
        }
        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Bundle bundle = new Bundle();
                
                bundle.putString(Notepadv2.KEY_TITLE, titleText.getText().toString());
                bundle.putString(Notepadv2.KEY_BODY, bodyText.getText().toString());
                if (rowId != null) {
                    bundle.putLong(Notepadv2.KEY_ROW_ID, rowId);
                }
                
                setResult(RESULT_OK, null, bundle);
                finish();
            }
           
        });
	}

}
