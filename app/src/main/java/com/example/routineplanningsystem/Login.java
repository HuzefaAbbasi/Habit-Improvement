package com.example.routineplanningsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Queue;


public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Configure sign-in options.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if the user is already signed in.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            String userName = account.getDisplayName();

            UserManager userManager = UserManager.getInstance();
            userManager.setUserName(userName);
            // User is already signed in, proceed to the main activity.
            goToMainActivity();
        }


    }

    public void onButtonClick(View view) {
        //As a guest login
        String userName = "Guest";
        UserManager userManager = UserManager.getInstance();
        userManager.setUserName(userName);
        goToMainActivity();
    }

    // Called when the login button is clicked.
    public void onLoginButtonClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with your own backend server or perform necessary operations.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String userName = account.getDisplayName();

                UserManager userManager = UserManager.getInstance();
                userManager.setUserName(userName);

                // Proceed to the main activity.
                goToMainActivity();
            } catch (ApiException e) {
                // Google Sign In failed, handle error.
                Log.d("Sign in Failed", "Google sign in failed", e);
                // Show an error message to the user.
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, ScheduleTabLayOutView.class);
        startActivity(intent);
    }



}
