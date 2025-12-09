package com.its.iam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {

    @Size(min = 3, max = 50, message = "USERNAME_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "USERNAME_INVALID_FORMAT")
    String username;

    @Email(message = "EMAIL_INVALID")
    String email;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "PHONE_INVALID")
    String phone;

    @Size(min = 1, max = 100, message = "DISPLAY_NAME_INVALID")
    String displayName;
}