/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mNameView;
    private TextInputLayout usernameText;
    private TextInputLayout nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        //Set font for header
        TextView loginHeader = (TextView) findViewById(R.id.login_header);
        loginHeader.setTypeface(font);
        usernameText = (TextInputLayout)  findViewById(R.id.textInputLayout);
        usernameText.setTypeface(font);
        nameText = (TextInputLayout)  findViewById(R.id.textInputLayout2);
        nameText.setTypeface(font);

        // Set up the login form
        sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        sharedPrefsEditor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        String username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        mUsernameView = (EditText) findViewById(R.id.username);
        mUsernameView.setTypeface(font);
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
        mUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                usernameText.setError(null);
            }
        });

        mNameView = (EditText) findViewById(R.id.name);
        mNameView.setTypeface(font);
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
        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                nameText.setError(null);
            }
        });

        if (username != null) {
            Log.i("Username found",username);
            FileController fc = new FileController();
            User user = fc.loadUser(getApplicationContext(), username);
            login(user);
        }

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setTypeface(font);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setTypeface(font);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

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
        //mUsernameView.setError(null);
        //mNameView.setError(null);
        usernameText.setError(null);
        nameText.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            //mUsernameView.setError(getString(R.string.error_field_required));
            usernameText.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!username.matches("[a-zA-Z0-9_.-]+")) {
            //mUsernameView.setError(getString(R.string.error_invalid_username));
            usernameText.setError(getString(R.string.error_invalid_username));
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
        usernameText.setError(null);
        nameText.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            //mNameView.setError(getString(R.string.error_field_required));
            nameText.setError("This field is required");
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            //mUsernameView.setError(getString(R.string.error_field_required));
            usernameText.setError("This field is required");
            focusView = mUsernameView;
            cancel = true;
        } else if (!username.matches("[a-zA-Z0-9_.-]+")) {
            //mUsernameView.setError(getString(R.string.error_invalid_username));
            usernameText.setError("This username contains invalid characters");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // kick off task to perform the user login attempt.
            mAuthTask = new UserLoginTask(username,name);
            mAuthTask.execute();
        }
    }

    /**
     * Calls the intent to move on to the main activity
     */
    private void login(User user) {
        FileController fc = new FileController();
        fc.saveUser(getApplicationContext(), user);
        Intent intent = new Intent(this, MainActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(user.getUsername());
        sharedPrefsEditor.putString("username", json);
        sharedPrefsEditor.commit();
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask {

        private ElasticSearchController.AddUserTask addUserTask = new ElasticSearchController.AddUserTask();
        private ElasticSearchController.GetUserTask getUserTask = new ElasticSearchController.GetUserTask();
        private final String mUsername;
        private final String mName;
        private User user;

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
            getUserTask.execute(username);
            try {
                user = getUserTask.get();
            } catch (Exception e) {
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
                try {
                    user = getUserTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                if (usernameExists(mUsername)) {
                    return false;
                }
                addUserTask.execute(mName, mUsername);
                try {
                    user = addUserTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        protected void execute() {
            Boolean success = verify();
            if (!success) {
                mAuthTask = null;
                if (mName == null) {
                    usernameText.setError(getString(R.string.error_incorrect_username));
                    //mUsernameView.setError(getString(R.string.error_incorrect_username));
                    mUsernameView.requestFocus();
                } else {
                    usernameText.setError(getString(R.string.error_username_exists));
                    //mUsernameView.setError(getString(R.string.error_username_exists));
                    mUsernameView.requestFocus();
                }
            } else {
                mAuthTask = null;
                login(user);
            }
        }
    }
}