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

    public Todo(int id, String todo) {
        this.id = id;
        this.todo = todo;
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
