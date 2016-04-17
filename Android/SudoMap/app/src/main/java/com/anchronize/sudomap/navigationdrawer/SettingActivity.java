package com.anchronize.sudomap.navigationdrawer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anchronize.sudomap.R;
import com.anchronize.sudomap.SudoMapApplication;
import com.anchronize.sudomap.objects.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class SettingActivity extends AppCompatActivity {
    private TextView nameTextView;
    private EditText nameEditText;
    private Button changeNameButton;
    private ImageView myImageView;
    private final int SELECT_PHOTO = 1;
    private FloatingActionButton myFab;
    private Bitmap selectedImage;

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
                selectedImage = BitmapFactory.decodeStream(imageStream);

                /**
                 * Delete the commented out portion if you do not need it
                 * */
//                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
//                Cursor cur = getContentResolver().query(imageUri, orientationColumn, null, null, null);
//                int orientation = -1;
//                if (cur != null && cur.moveToFirst()) {
//                    orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
//                }
//                Matrix matrix = new Matrix();
//                matrix.postRotate(orientation);
//                selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);

                changePicOrientation(imageUri, selectedImage);

                // Converting to string to push to firebase
                Bitmap copySelectedImage = selectedImage;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copySelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String imgString = Base64.encodeToString(b, Base64.DEFAULT);
                User current = ((SudoMapApplication) getApplication()).getCurrentUser();
                current.setProfileImgString(imgString);
                //TODO Update current user

//                myImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Fixes picture orientation issue so now all pictures show up correctly
    public void changePicOrientation(Uri imageUri, Bitmap selectedImage){
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = getContentResolver().query(imageUri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(),
                selectedImage.getHeight(), matrix, true);
        myImageView.getLayoutParams().height = myImageView.getMeasuredHeight();
        myImageView.getLayoutParams().width = myImageView.getMeasuredWidth();
        myImageView.setImageBitmap(selectedImage);
    }


    public void onClickPic(View view)
    {
//        Toast.makeText(this, "You clicked me!", Toast.LENGTH_SHORT).show();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(),
                selectedImage.getHeight(), matrix, true);
        myImageView.setImageBitmap(selectedImage);

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