package org.example.crudApplication.dto;

import org.example.crudApplication.models.Task;
import org.example.crudApplication.models.User;

public class TaskMapper {
    public static TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
        }

        return dto;
    }

    public static Task toEntity(TaskDto dto, User user) {
        if (dto == null) {
            return null;
        }

        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setUser(user);

        return task;
    }
}
