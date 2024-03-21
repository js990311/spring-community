package com.toyproject.community.repository;

import com.toyproject.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.post.id = :postId order by c.groupId, c.sequence")
    List<Comment> findByPostId(Long postId);

    List<Comment> findByMemberId(Long memberId);

    @Query(value = "select c from Comment c where c.parentComment = :parentComment and c.sequence = (select max(sub.sequence) from Comment sub where sub.parentComment = :parentComment)")
    List<Comment> getAvailableSequence(Comment parentComment);

    @Modifying
    @Query(value = "update comments set sequence = sequence + 1 where group_id = :groupId and sequence >= :sequence", nativeQuery = true)
    int increaseSequence(Long groupId, Long sequence);

    @Query("select max(c.groupId) from Comment c where c.post.id = :postId")
    Long getAvailableGroupId(Long postId);

    @Modifying
    @Query(value = "update comments set sequence = sequence - 1 where group_id = :groupId and sequence >= :sequence", nativeQuery = true)
    int decreaseSequence(Long groupId, Long sequence);

    boolean existsDeletedCommentByIsDeletedTrueAndChildCount(long childCount);

    Comment findDeletedParentCommentByIsDeletedTrueAndChildCount(long childCount);
}
