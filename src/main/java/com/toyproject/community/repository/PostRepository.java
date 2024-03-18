package com.toyproject.community.repository;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Comment;
import com.toyproject.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByBoardId(Long boardId);

    @Query(value = "select p from Post p join fetch p.board b join fetch p.member m where b.id = :boardId",
    countQuery = "select count(p) from Post p join p.board b where b.id = :boardId")
    Page<Post> findPageByBoardId(Long boardId, Pageable pageable);
    List<Post> findByMemberId(Long memberId);

    Page<Post> findPageByMemberId(Long memberId, Pageable pageable);

    @Query("select p from Post p join p.board b where b.name = :boardName")
    List<Post> findByBoardName(@Param("boardName") String name);
}
