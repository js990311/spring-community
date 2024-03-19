package com.toyproject.community.repository;

import com.toyproject.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.post.id = :postId order by c.creationDateTime, c.parentComment.id")
    List<Comment> findByPostId(Long postId);

    List<Comment> findByMemberId(Long memberId);
}
