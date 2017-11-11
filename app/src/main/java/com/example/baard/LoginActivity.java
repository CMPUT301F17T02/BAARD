/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mNameView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mUsernameView = (EditText) findViewById(R.id.username);
        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mNameView = (EditText) findViewById(R.id.name);
        mNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in to the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mNameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!username.matches("[a-zA-Z0-9_.-]+")) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(username);
            mAuthTask.execute();
        }
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (duplicate username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mNameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!username.matches("[a-zA-Z0-9_.-]+")) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(username,name);
            mAuthTask.execute();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    //private void showProgress(final boolean show) {
    //    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    //    // for very easy animations. If available, use these APIs to fade-in
    //    // the progress spinner.
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
    //        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

    //        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    //        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
    //                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
    //            @Override
    //            public void onAnimationEnd(Animator animation) {
    //                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    //            }
    //        });

    //        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    //        mProgressView.animate().setDuration(shortAnimTime).alpha(
    //                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
    //            @Override
    //            public void onAnimationEnd(Animator animation) {
    //                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    //            }
    //        });
    //    } else {
    //        // The ViewPropertyAnimator APIs are not available, so simply show
    //        // and hide the relevant UI components.
    //        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    //        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    //    }
    //}

    /**
     * Calls the intent to move on to the main activity
     */
    private void login(User user) {
        // TODO: Pass intent to main activity with user instance
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(user);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask {
        ElasticSearchController.AddUserTask addUserTask = new ElasticSearchController.AddUserTask();

        private final String mUsername;
        private final String mName;

        UserLoginTask(String username, String name) {
            mUsername = username;
            mName = name;
        }

        UserLoginTask(String username) {
            mUsername = username;
            mName = null;
        }

        private boolean usernameExists(String username) {
            User user = null;
            ElasticSearchController.GetUserTask getUserTask = new ElasticSearchController.GetUserTask();
            getUserTask.execute(username);
            try {
                user = getUserTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (user == null) {
                return false;
            }
            return true;
        }

        protected Boolean verify() {
            if (mName == null) {
                if (!usernameExists(mUsername)) {
                    return false;
                }
            } else {
                if (usernameExists(mUsername)) {
                    return false;
                }
                // TODO: Register new user
                User user = null;
                addUserTask.execute(mName, mUsername);
                /*try {
                    user = addUserTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("elasticSearch", "Unable to find user with name");
                }*/
            }
            return true;
        }

        protected void execute() {
            Boolean success = verify();
            if (!success) {
                mAuthTask = null;
                //showProgress(false);

                if (mName == null) {
                    mUsernameView.setError(getString(R.string.error_incorrect_username));
                    mUsernameView.requestFocus();
                } else {
                    mUsernameView.setError(getString(R.string.error_username_exists));
                    mUsernameView.requestFocus();
                }
            } else {
                mAuthTask = null;
                //showProgress(false);
                try {
                    login(addUserTask.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        //protected void onCancelled() {
        //    mAuthTask = null;
        //    //showProgress(false);
        //}
    }
}