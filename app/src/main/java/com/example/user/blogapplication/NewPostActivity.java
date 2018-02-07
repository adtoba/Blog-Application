package com.example.user.blogapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewPostActivity extends AppCompatActivity {

    private static int GALLERY_PICK = 1;

    //Layout and widget references
    private ImageButton mImageButton;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private ProgressDialog mProgress;

    // Uri
    private Uri imageUri = null;

    // Other references
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.new_post_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Button references
        mImageButton = (ImageButton) findViewById(R.id.add_image);
        mSubmitButton = (Button) findViewById(R.id.btn_submit);

        // Edittext references
        mPostTitle = (EditText) findViewById(R.id.post_title);
        mPostDesc = (EditText) findViewById(R.id.post_desc);

        // Other references
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);



    }


    public void addImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    public void setBtnSubmitPost(View view) {
        startPosting();
    }

    private void startPosting() {

        mProgress.setMessage("Posting to Blog.....");
        mProgress.show();

        String title_text = mPostTitle.getText().toString();
        String desc_text = mPostDesc.getText().toString();

        if (!TextUtils.isEmpty(title_text) && !TextUtils.isEmpty(desc_text) && imageUri != null) {

            StorageReference filepath = mStorage.child("Blog-Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            mProgress.dismiss();

                            Toast.makeText(NewPostActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();

                        }

                    });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();

            mImageButton.setImageURI(imageUri);
        }
    }
}
