package com.example.user.blogapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView mBlogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Firebase Blog App");

        // Firebase References
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        // Recycler View
        mBlogList = (RecyclerView) findViewById(R.id.mBlogList);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToSignUp();
        }

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                        Blog.class,
                        R.layout.single_post,
                        BlogViewHolder.class,
                        mDatabase

                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                    }
                };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    private static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        private void setTitle(String title) {
            TextView title_text = (TextView) mView.findViewById(R.id.single_post_title);
            title_text.setText(title);

        }

        private void setDesc(String desc) {
            TextView desc_text = (TextView) mView.findViewById(R.id.single_post_text);
            desc_text.setText(desc);

        }

        private void setImage(Context context, String image) {
            ImageButton image_button = (ImageButton) mView.findViewById(R.id.single_post_image);
            Picasso.with(context).load(image).into(image_button);

        }
    }

    private void sendToSignUp() {
        Intent signUpIntent = new Intent(this, RegisterActivity.class);
        signUpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signUpIntent);
    }

    private void sendToLogin() {
        Intent signInIntent = new Intent(this, LoginActivity.class);
        signInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signInIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            sendToSignUp();
        }

        return super.onOptionsItemSelected(item);
    }


}
