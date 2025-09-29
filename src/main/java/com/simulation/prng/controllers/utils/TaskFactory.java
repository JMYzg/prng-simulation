package com.simulation.prng.controllers.utils;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class TaskFactory {

    public static <T> Task<T> create(Callable<T> logic, Consumer<T> success, Consumer<Throwable> failure) {
        Task<T> task = new Task<T>() {

            @Override
            protected T call() throws Exception {
                return logic.call();
            }
        };

        task.setOnSucceeded(e -> {
            success.accept(task.getValue());
        });

        task.setOnFailed(e -> {
            failure.accept(task.getException());
        });

        return task;
    }
}
