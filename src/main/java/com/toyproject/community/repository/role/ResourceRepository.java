package com.toyproject.community.repository.role;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.role.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource,Long> {
    Optional<Resource> findByUrl(String url);

    @Query("select r from Resource r join fetch r.resourceRoles order by r.orderNum desc")
    List<Resource> findAllResource();
}
