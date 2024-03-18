package com.toyproject.community.domain;

import com.toyproject.community.domain.dto.CreatePostDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
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

    @Column(columnDefinition = "integer default 0")
    private Long viewCount;

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

    public void updatePost(String title, String content){
        this.content = content;
        this.title = title;
        this.modificationDateTime = LocalDateTime.now();
    }

    public void increasePostViewCount(){
        log.debug("increasePostView");
        this.viewCount += 1;
    }

    public static Post createPost(CreatePostDto postDto){
        Post post = new Post(postDto);
        return post;
    }
}
