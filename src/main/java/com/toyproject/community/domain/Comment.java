package com.toyproject.community.domain;

import com.toyproject.community.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;

    private void setContent(String content){
        this.content = content;
    }

    Comment(CommentDto commentDto){
        this.content = commentDto.getContent();
        this.post = commentDto.getPost();
        this.member = commentDto.getWriter();
    }

    public static Comment createComment(CommentDto commentDto){
        return new Comment(commentDto);
    }

    public void updateComment(String content){
        setContent(content);
    }
}
