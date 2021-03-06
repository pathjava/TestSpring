package ru.progwards.lesson1.task.annotationsconfig;

import java.util.List;

public interface TaskRepository {
    void save(Task task);

    void update(Task task);

    void delete(String id);

    List<Task> getAll();

    Task get(String id);
}
