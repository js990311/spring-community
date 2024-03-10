package com.toyproject.community.domain;

import com.toyproject.community.domain.dto.CreatePostDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @Column(name = "modification_date_time")
    private LocalDateTime modificationDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member member;


    /**
     * domain 패키지 외부에선 조작하지 못하도록 protected 접근 제한자
     * @param postDto
     */
    Post(CreatePostDto postDto){
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.board = postDto.getBoard();
        this.member = postDto.getMember();
        this.creationDateTime = LocalDateTime.now();
    }

    private void setTitle(String title){
        this.title = title;
    }

    private void setContent(String content){
        this.content = content;
    }

    public void updatePost(String title, String content){
        setContent(content);
        setTitle(title);
        this.modificationDateTime = LocalDateTime.now();
    }

    public static Post createPost(CreatePostDto postDto){
        Post post = new Post(postDto);
        return post;
    }
}
