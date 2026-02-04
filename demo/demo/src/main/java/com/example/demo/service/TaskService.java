package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.exception.TaskNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        Task saved = taskRepository.save(task);
        return new TaskResponse(saved.getId(), saved.getTitle(), saved.getDescription(), saved.isCompleted());
    }

      public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(t -> new TaskResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.isCompleted()
                ))
                .toList();
 }

 public TaskResponse getTaskById(Long id) {
    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

    return TaskResponse.fromEntity(task);
}

public TaskResponse updateTask(Long id, TaskRequest request) {
    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setCompleted(request.isCompleted());

    Task updated = taskRepository.save(task);
    return TaskResponse.fromEntity(updated);
}

public void deleteTask(Long id) {
    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

    taskRepository.delete(task);
}


}