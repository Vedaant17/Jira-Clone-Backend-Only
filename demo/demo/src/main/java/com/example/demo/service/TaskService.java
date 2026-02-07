package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.tenant.TenantContext;

import org.springframework.stereotype.Service;
import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = TaskMapper.toEntity(request);
        task.setTenantId(TenantContext.getTenantId());
        task.setCompleted(request.isCompleted());
        task.setTenantId(TenantContext.getTenantId());
        Task saved = taskRepository.save(task);
        return TaskResponse.fromEntity(saved);
    }

      public List<TaskResponse> getAllTasks() {
    String tenantId = TenantContext.getTenantId();
    return taskRepository.findByTenantId(tenantId)
            .stream()
            .map(TaskMapper::toResponse)
            .toList();
}
 

 public TaskResponse getTaskById(Long id) {
    String tenantId = TenantContext.getTenantId();

    Task task = taskRepository
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new TaskNotFoundException(id));

    return TaskResponse.fromEntity(task);
}


public TaskResponse updateTask(Long id, TaskRequest request) {

    String tenantId = TenantContext.getTenantId();

    Task task = taskRepository
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new TaskNotFoundException(id));

    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setCompleted(request.isCompleted());

    Task updated = taskRepository.save(task);
    return TaskResponse.fromEntity(updated);
}


public void deleteTask(Long id) {

    String tenantId = TenantContext.getTenantId();

    Task task = taskRepository
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new TaskNotFoundException(id));

    taskRepository.delete(task);
}



}