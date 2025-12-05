package com.its.iam.mapper;

import com.its.iam.dto.response.UserDto;
import com.its.iam.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserDto mapToUserDto(User user){
        String roleName = user.getRole() != null ? user.getRole().getName() : null;

        return UserDto.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .username(user.getUsername())
                .role(roleName)
                .build();
    }
}
