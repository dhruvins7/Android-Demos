package com.example.user.demoproject.model_classes;

/**
 * Created by user on 08-05-2017.
 */

public class TaskModel {
    int id;
    String task;
    boolean checked;
    int idList;
    String list;
    int userid;
    String userEmail;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", checked=" + checked +
                '}';
    }
}
