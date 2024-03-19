package com.toyproject.community.domain;

import com.toyproject.community.domain.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;

    @Column
    private Long depth;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    /* FK */

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;

    private void setContent(String content){
        this.content = content;
    }

    void setParentComment(Comment parentComment){
        this.parentComment = parentComment;
        this.depth = this.parentComment.getDepth() + 1;
    }

    Comment(CommentDto commentDto){
        this.content = commentDto.getContent();
        this.post = commentDto.getPost();
        this.member = commentDto.getWriter();
        this.depth = 0l;
        this.creationDateTime = LocalDateTime.now();
    }

    public static Comment createComment(CommentDto commentDto){
        return new Comment(commentDto);
    }

    public static void setParentComment(Comment comment, Comment parentComment){
        comment.setParentComment(parentComment);
    }

    public void updateComment(String content){
        setContent(content);
    }
}
