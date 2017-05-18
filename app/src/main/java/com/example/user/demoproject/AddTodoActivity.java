package com.example.user.demoproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.user.demoproject.databinding.ActivityAddTodoBinding;
import com.example.user.demoproject.model_classes.ListModel;
import com.example.user.demoproject.model_classes.TaskModel;
import com.example.user.demoproject.model_classes.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 08-05-2017.
 */

public class AddTodoActivity extends AppCompatActivity {
    ActivityAddTodoBinding activityAddTodoBinding;

    DbHelper dbHelper;
    public List<ListModel> labelsStrings;
    private int idList;
    long id;
    private int id_user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddTodoBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_todo);
        activityAddTodoBinding.setAddtodo(this);
        SharedPreferences prefs =  getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        id_user = prefs.getInt("id",0);
        dbHelper = new DbHelper(this);
        activityAddTodoBinding.spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListModel model = labelsStrings.get(position);
                idList = model.getId();//Getting the current Id of Selected option in spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        loadSpinnerData();

    }

    @Override
    protected void onResume() {
        loadSpinnerData();
        super.onResume();
    }

    public void submit() {
        String task = activityAddTodoBinding.editTask.getText().toString();
        TaskModel taskModel = new TaskModel();
        taskModel.setTask(task);
        taskModel.setIdList(idList);//Setting it in the Model with other Task
        taskModel.setUserid(id_user);
        dbHelper.addTask(taskModel);//Setting the values from the model class to database
        setResult(RESULT_OK);
        finish();

    }

    public void listcall() {
        Intent intent = new Intent(this, AddListActivity.class);
        startActivity(intent);
    }

    private void loadSpinnerData() {
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        labelsStrings = dbHelper.getAllLabels(id_user);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < labelsStrings.size(); i++) {
            strings.add(labelsStrings.get(i).getList());//Getting it from the list created from database
        }
        ArrayAdapter<String> daAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);

        daAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        activityAddTodoBinding.spinnerList.setAdapter(daAdapter);


    }

}
