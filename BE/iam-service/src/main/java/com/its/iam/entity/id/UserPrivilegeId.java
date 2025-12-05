package com.its.iam.entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class UserPrivilegeId {
    private Long userId;
    private Long privilegeId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPrivilegeId that = (UserPrivilegeId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(privilegeId, that.privilegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, privilegeId);
    }
}
