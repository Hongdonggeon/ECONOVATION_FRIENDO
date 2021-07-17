package org.techtown.myapplication;

import java.util.Objects;

public class Todo {
    public String pushKey;
    public String todo;
    public String alarm;
    public boolean checkBoxChecked;
    public boolean alarmChecked;

    public Todo(){}

    public Todo(String pushKey, String todo, String alarm, boolean checkBoxChecked, boolean alarmChecked) {
        this.pushKey = pushKey;
        this.todo = todo;
        this.alarm = alarm;
        this.checkBoxChecked = checkBoxChecked;
        this.alarmChecked = alarmChecked;
    }

    public Todo(String todo, String alarm, boolean checkBoxChecked, boolean alarmChecked) {
        this.todo = todo;
        this.alarm = alarm;
        this.checkBoxChecked = checkBoxChecked;
        this.alarmChecked = alarmChecked;
    }

    public Todo(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
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

    @Override
    public String toString() {
        return "Todo{" +
                "todo='" + todo + '\'' +
                ", alarm='" + alarm + '\'' +
                ", checkBoxChecked=" + checkBoxChecked +
                ", alarmChecked=" + alarmChecked +
                '}';
    }
}
