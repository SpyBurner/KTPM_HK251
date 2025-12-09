package com.its.iam.repository;

import com.its.iam.entity.Privilege;
import com.its.iam.entity.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);
    List<Privilege> findByType(PrivilegeType type);
    boolean existsByName(String name);
}