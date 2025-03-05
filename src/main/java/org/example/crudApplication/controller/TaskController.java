package org.example.crudApplication.controller;

import org.example.crudApplication.dto.TaskDto;
import org.example.crudApplication.dto.TaskMapper;
import org.example.crudApplication.models.Task;
import org.example.crudApplication.models.User;
import org.example.crudApplication.repository.TaskRepository;
import org.example.crudApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        User user = null;
        if (taskDto.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(taskDto.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }
            user = userOpt.get();
        }

        Task task = TaskMapper.toEntity(taskDto, user);

        Task savedTask = taskRepository.save(task);

        return ResponseEntity.ok(TaskMapper.toDto(savedTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto updatedTaskDto) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task task = taskOpt.get();

        task.setTitle(updatedTaskDto.getTitle());
        task.setDescription(updatedTaskDto.getDescription());
        task.setCompleted(updatedTaskDto.isCompleted());

        if (updatedTaskDto.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(updatedTaskDto.getUserId());
            userOpt.ifPresent(task::setUser);
        }

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(TaskMapper.toDto(savedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted");
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TaskMapper.toDto(taskOpt.get()));
    }
}
