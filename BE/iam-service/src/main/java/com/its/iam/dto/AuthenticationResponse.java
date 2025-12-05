package com.its.iam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.its.iam.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private UserDto userDto;
}
