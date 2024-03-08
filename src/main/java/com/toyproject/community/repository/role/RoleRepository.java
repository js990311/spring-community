package com.toyproject.community.repository.role;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.role.Role;
import com.toyproject.community.domain.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleName roleName);

    @Query("select r from Role r join fetch r.parentRole pr where r.parentRole is not null")
    List<Role> findChildRole();
}
