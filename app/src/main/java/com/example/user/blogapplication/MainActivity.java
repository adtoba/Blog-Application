package com.example.user.blogapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToSignUp();
        }

    }

    private static class BlogViewHolder extends RecyclerView.ViewHolder {

        public BlogViewHolder(View itemView) {
            super(itemView);
        }

        private void setTitle(String title) {

        }

        private void setDesc(String desc) {

        }

        private void setImage(Context context, String image) {

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
            sendToLogin();
        }

        return super.onOptionsItemSelected(item);
    }


}
