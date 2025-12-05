package com.its.iam.repository;

import com.its.iam.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    
    Optional<File> findByName(String name);
    
    List<File> findByBucketId(String bucketId);
    
    List<File> findByNameContainingIgnoreCase(String namePattern);
    
    boolean existsByName(String name);
    
    boolean existsByBucketId(String bucketId);
    
    void deleteByBucketId(String bucketId);
}