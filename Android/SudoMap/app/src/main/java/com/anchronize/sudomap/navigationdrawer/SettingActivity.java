package com.anchronize.sudomap.navigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anchronize.sudomap.NavigationDrawer;
import com.anchronize.sudomap.R;

import org.w3c.dom.Text;

public class SettingActivity extends NavigationDrawer {
    private TextView nameTextView;
    private EditText nameEditText;
    private Button changeNameButton, changePicButton;
    private ImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        nameTextView = (TextView)findViewById(R.id.nameTextView);
        nameEditText = (EditText)findViewById(R.id.change_name_text);
        changeNameButton = (Button)findViewById(R.id.change_name_button);
        changePicButton = (Button)findViewById(R.id.change_pic_button);
        myImageView = (ImageView)findViewById(R.id.profile_pic);

        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    String newName = nameEditText.getText().toString();
                    nameTextView.setText(newName);
                    return true;
                }
                return false;
            }
        });

        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEditText.getText().toString();
                nameTextView.setText(newName);
            }
        });

        changePicButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_ENTER:
                Toast.makeText(this, "Enter key pushed!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}