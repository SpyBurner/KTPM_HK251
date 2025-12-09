package com.its.media.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaFileDetailResponse {
    UUID id;
    String name;
    String bucketId;
    String username;
    boolean profilePicture;
}
