package com.anchronize.sudomap.navigationdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingActivity extends NavigationDrawer {
    private TextView nameTextView;
    private EditText nameEditText;
    private Button changeNameButton;
    private ImageView myImageView;
    private final int SELECT_PHOTO = 1;
    private FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        nameTextView = (TextView) findViewById(R.id.nameTextView);
//        nameEditText = (EditText) findViewById(R.id.change_name_text);
//        changeNameButton = (Button) findViewById(R.id.change_name_button);
        myFab = (FloatingActionButton) findViewById(R.id.change_pic_button);
        myImageView = (ImageView) findViewById(R.id.profile_pic);
//
//        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    String newName = nameEditText.getText().toString();
//                    nameTextView.setText(newName);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        changeNameButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newName = nameEditText.getText().toString();
//                nameTextView.setText(newName);
//            }
//        });

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        /**
//         * @Rohan
//         * Delete this commented out code snippet if no problems arise
//         * IF YOU SEE THIS COMMENTED OUT CODE RIGHT BEFORE WE SUBMIT THEN TELL ROHAN IMMEDIATELY
//         * */
////        switch(requestCode) {
////            case SELECT_PHOTO:
////                if(resultCode == RESULT_OK){
////                    try {
////                        final Uri imageUri = imageReturnedIntent.getData();
////                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
////                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////                        //TODO set current user profile image to selectedImage.toString or something
////                        myImageView.setImageBitmap(selectedImage);
////                        myImageView.getLayoutParams().height = myImageView.getMeasuredHeight();
////                        myImageView.getLayoutParams().width = myImageView.getMeasuredWidth();
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    }
////
////                }
////        }
        if (resultCode == RESULT_OK) {
            try {
                Uri imageUri = imageReturnedIntent.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                // Converting to string to push to firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String string = Base64.encodeToString(b, Base64.DEFAULT);
                // End of pushing to firebase
                //TODO set current user profile image to selectedImage.toString or something
                myImageView.getLayoutParams().height = myImageView.getMeasuredHeight();
                myImageView.getLayoutParams().width = myImageView.getMeasuredWidth();

                RotateBitmap(selectedImage, 90);

                myImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
/**
 * @Rohan Delete this code snippet if no errors arise
 * IF YOU SEE THIS COMMENTED OUT CODE RIGHT BEFORE WE SUBMIT THEN TELL ROHAN IMMEDIATELY
 */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }
}