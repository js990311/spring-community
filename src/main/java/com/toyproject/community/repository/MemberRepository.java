package com.toyproject.community.repository;

import com.toyproject.community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmailOrNickname(String email, String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
