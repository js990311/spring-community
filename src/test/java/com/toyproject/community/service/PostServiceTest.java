package com.toyproject.community.service;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.Post;
import com.toyproject.community.domain.dto.CreatePostDto;
import com.toyproject.community.domain.view.ReadPostDto;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Slf4j
class PostServiceTest {
    

    @Autowired private EntityManager em;
    @Autowired private PostService postService;

    @Test
    void createPost() {
        Member member = Member.registMember("testMember", "1234", "nickname");
        Board board = Board.createBoard("testBoard", "test");

        em.persist(member);
        em.persist(board);

        CreatePostDto postDto = new CreatePostDto(member, board, "title", "content");

        Long postId = postService.createPost(postDto);
        Post post = postService.readPostById(postId);

        assertEquals(post.getTitle(), "title", "title값 입력 확인");
        assertEquals(post.getContent(), "content", "content값 입력 확인");
        assertEquals(post.getMember().getId(), member.getId(), "memberId test");
        assertEquals(post.getBoard().getId(), board.getId(), "boardId test");
    }

    @Test
    void readPostByBoard() {
        Member member = Member.registMember("testMember", "1234", "nickname");
        Board board1 = Board.createBoard("testBoard1", "test");
        Board board2 = Board.createBoard("testBoard2", "test");

        em.persist(member);
        em.persist(board1);
        em.persist(board2);

        CreatePostDto postDto1 = new CreatePostDto(member, board1, "board1 title", "content");
        CreatePostDto postDto2 = new CreatePostDto(member, board2, "board2 title", "content");

        Long postId1 = postService.createPost(postDto1);
        Long postId2 = postService.createPost(postDto2);

        Post post1 = postService.readPostById(postId1);
        Post post2 = postService.readPostById(postId2);

        List<ReadPostDto> postsInBoard1 = postService.readPostByBoardId(board1.getId()).getContent();
        List<ReadPostDto> postsInBoard2 = postService.readPostByBoardId(board2.getId()).getContent();

        assertEquals(postsInBoard1.get(0).getBoard().getId(), board1.getId(), "board1 test");
        assertEquals(postsInBoard2.get(0).getBoard().getId(), board2.getId(), "board2 test");

        assertEquals(postsInBoard1.get(0).getId(), post1.getId());
        assertEquals(postsInBoard2.get(0).getId(), post2.getId());
    }

    @Test
    void readPostByBoardName(){
        String boardName1 = "testBoard1";
        String boardName2 = "testBoard2";
        Member member = Member.registMember("testMember", "1234", "nickname");
        Board board1 = Board.createBoard(boardName1, "test");
        Board board2 = Board.createBoard(boardName2, "test");

        em.persist(member);
        em.persist(board1);
        em.persist(board2);

        CreatePostDto postDto1 = new CreatePostDto(member, board1, "board1 title", "content");
        CreatePostDto postDto2 = new CreatePostDto(member, board2, "board2 title", "content");

        Long postId1 = postService.createPost(postDto1);
        Long postId2 = postService.createPost(postDto2);

        // test
        Post post1 = postService.readPostById(postId1);
        Post post2 = postService.readPostById(postId2);

        log.info("readPostByBoardName 1");
        List<Post> postsInBoard1 = postService.readPostByBoardName(board1.getName());
        log.info("readPostByBoardName 2");
        List<Post> postsInBoard2 = postService.readPostByBoardName(board2.getName());

        assertEquals(postsInBoard1.get(0).getBoard().getId(), board1.getId(), "board1 test");
        assertEquals(postsInBoard2.get(0).getBoard().getId(), board2.getId(), "board2 test");

        assertEquals(postsInBoard1.get(0).getId(), post1.getId());
        assertEquals(postsInBoard2.get(0).getId(), post2.getId());

        log.info("member loading test");
        assertEquals(member.getId(), postsInBoard1.get(0).getMember().getId(),"memberId Test1");
        assertEquals(member.getId(), postsInBoard2.get(0).getMember().getId(),"memberId Test2");
    }

    @Test
    void updatePost() {
        Member member = Member.registMember("testMember", "1234", "nickname");
        Board board = Board.createBoard("testBoard", "test");

        em.persist(member);
        em.persist(board);

        CreatePostDto postDto = new CreatePostDto(member, board, "title", "content");

        Long postId = postService.createPost(postDto);

        postService.updatePost(postId, "updateTitle", "updateContent");

        Post post = postService.readPostById(postId);
        assertEquals(post.getTitle(), "updateTitle", "title이 업데이트 되었는지 확인");
        assertEquals(post.getContent(), "updateContent", "content가 업데이트 되었는지 확인");
    }
}