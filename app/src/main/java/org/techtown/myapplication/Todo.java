package org.techtown.myapplication;

import java.util.Objects;

public class Todo {
    public int id;
    public String todo;
    public String alarm;

    public Todo(int id, String todo, String alarm) {
        this.id = id;
        this.todo = todo;
        this.alarm = alarm;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
