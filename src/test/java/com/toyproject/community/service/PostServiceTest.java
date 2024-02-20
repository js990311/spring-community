package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.dto.PostDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class PostServiceTest {
    

    @Autowired private EntityManager em;
    @Autowired private PostService postService;

    @Test
    void createPost() {
        Member member = Member.registMember("testMember", "1234");
        Board board = Board.createBoard("testBoard");

        em.persist(member);
        em.persist(board);

        PostDto postDto = new PostDto(member, board, "title", "content");

        Long postId = postService.createPost(postDto);
        Post post = postService.readPostById(postId);

        assertEquals(post.getTitle(), "title", "title값 입력 확인");
        assertEquals(post.getContent(), "content", "content값 입력 확인");
        assertEquals(post.getMember().getId(), member.getId(), "memberId test");
        assertEquals(post.getBoard().getId(), board.getId(), "boardId test");
    }

    @Test
    void readPostByBoard() {
        Member member = Member.registMember("testMember", "1234");
        Board board1 = Board.createBoard("testBoard1");
        Board board2 = Board.createBoard("testBoard2");

        em.persist(member);
        em.persist(board1);
        em.persist(board2);

        PostDto postDto1 = new PostDto(member, board1, "board1 title", "content");
        PostDto postDto2 = new PostDto(member, board2, "board2 title", "content");

        Long postId1 = postService.createPost(postDto1);
        Long postId2 = postService.createPost(postDto2);

        Post post1 = postService.readPostById(postId1);
        Post post2 = postService.readPostById(postId2);

        List<Post> postsInBoard1 = postService.readPostByBoard(board1.getId());
        List<Post> postsInBoard2 = postService.readPostByBoard(board2.getId());

        assertEquals(postsInBoard1.get(0).getBoard().getId(), board1.getId(), "board1 test");
        assertEquals(postsInBoard2.get(0).getBoard().getId(), board2.getId(), "board2 test");

        assertEquals(postsInBoard1.get(0).getId(), post1.getId());
        assertEquals(postsInBoard2.get(0).getId(), post2.getId());
    }

    @Test
    void updatePost() {
        Member member = Member.registMember("testMember", "1234");
        Board board = Board.createBoard("testBoard");

        em.persist(member);
        em.persist(board);

        PostDto postDto = new PostDto(member, board, "title", "content");

        Long postId = postService.createPost(postDto);

        postService.updatePost(postId, "updateTitle", "updateContent");

        Post post = postService.readPostById(postId);
        assertEquals(post.getTitle(), "updateTitle", "title이 업데이트 되었는지 확인");
        assertEquals(post.getContent(), "updateContent", "content가 업데이트 되었는지 확인");
    }
}