package com.its.iam.repository;

import com.its.iam.entity.Privilege;
import com.its.iam.entity.User;
import com.its.iam.entity.UserHasPrivilege;
import com.its.iam.entity.id.UserPrivilegeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHasPrivilegeRepository extends JpaRepository<UserHasPrivilege, UserPrivilegeId> {
    
    List<UserHasPrivilege> findByUserId(Long userId);
    
    List<UserHasPrivilege> findByPrivilegeId(Long privilegeId);
    
    @Query("SELECT uhp.privilege FROM UserHasPrivilege uhp WHERE uhp.user.id = :userId")
    List<Privilege> findPrivilegesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT uhp.user FROM UserHasPrivilege uhp WHERE uhp.privilege.id = :privilegeId")
    List<User> findUsersByPrivilegeId(@Param("privilegeId") Long privilegeId);
    
    boolean existsByUserIdAndPrivilegeId(Long userId, Long privilegeId);
    
    void deleteByUserIdAndPrivilegeId(Long userId, Long privilegeId);
}