package org.techtown.myapplication;

import java.util.Objects;

public class Todo {
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String todo;
    public String alarm;
    public boolean checkBoxChecked;
    public boolean alarmChecked;

    public boolean isCheckBoxChecked() {
        return checkBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        this.checkBoxChecked = checkBoxChecked;
    }

    public boolean isAlarmChecked() {
        return alarmChecked;
    }

    public void setAlarmChecked(boolean alarmChecked) {
        this.alarmChecked = alarmChecked;
    }

    public Todo(){}

    public Todo(int id, String todo, String alarm, boolean checkBoxChecked, boolean alarmChecked) {
        this.id = id;
        this.todo = todo;
        this.alarm = alarm;
        this.checkBoxChecked = checkBoxChecked;
        this.alarmChecked = alarmChecked;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo1 = (Todo) o;
        return Objects.equals(todo, todo1.todo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todo);
    }
}
