package com.example.routineplanningsystem;

public class Task {
    private String taskName;
    private String taskDescription;
    private int taskType;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public Task(String taskName, String taskDescription, int taskType) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskType=" + taskType +
                '}';
    }
}
