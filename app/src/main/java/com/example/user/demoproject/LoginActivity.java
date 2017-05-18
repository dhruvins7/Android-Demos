package com.example.user.demoproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.demoproject.databinding.ActivitySharedPrefBinding;
import com.example.user.demoproject.model_classes.UserModel;

/**
 * Created by user on 04-05-2017.
 */

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private UserModel userModel;
    private ActivitySharedPrefBinding sharedPrefBinding;
    DbHelper dbHelper;
    public static String name = "name";
    String TAG = "DEBUG";
    public static final String MyPREFERENCES = "MyPrefs";
    private EditText editText;
    public EditText editEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shared_pref);
        sharedPrefBinding = DataBindingUtil.setContentView(this, R.layout.activity_shared_pref);
        sharedPrefBinding.setLogin(this);
        userModel = new UserModel();
        dbHelper = new DbHelper(this);

        editText = (EditText) findViewById(R.id.edit_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d(TAG, "PREFERENCES CALLED ");


        if (sharedPreferences.contains("id")) {
            Intent i = new Intent(this, MainActivity.class);

            Log.d(TAG, "MAINACTIVITY");
            startActivity(i);

            finish();
        }
    }

    public void submit() {
        DbHelper dbHelper = new DbHelper(this);
        String edit_name = editText.getText().toString();
        String edit_email = editEmail.getText().toString();
        int ij = dbHelper.checkUser(edit_email);
        if (ij > 0) {

            Intent i = new Intent(this, MainActivity.class);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", ij);
            editor.putString("email", edit_email);
            editor.apply();

            Log.d(TAG, "MAINACTIVITY");
            startActivity(i);
            finish();

        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
            if (edit_email.matches(emailPattern)) {
                userModel.setUserName(edit_name);
                userModel.setUserEmail(edit_email);
                int id = (int) dbHelper.addUser(userModel);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", id);
                editor.putString("email", edit_email);
                editor.apply();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
