package org.example.crudApplication.dto;

import org.example.crudApplication.models.User;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        if (user.getTasks() != null) {
            dto.setTasks(
                    user.getTasks().stream()
                            .map(TaskMapper::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());

        return user;
    }
}
