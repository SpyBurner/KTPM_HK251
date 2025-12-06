package com.its.media.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaFileUploadRequest {
    String publicUrl;
    String name;
    Long sectionId;
    boolean isProfilePicture;
}
