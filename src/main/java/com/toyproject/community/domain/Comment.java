package com.toyproject.community.domain;

import com.toyproject.community.domain.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    /* 일반 속성들 */

    @Column
    private String content;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @Column
    private Boolean isDeleted;

    /* 대댓글 계층 구조를 위한 속성 */

    @Column
    private Long groupId;

    @Column
    private Long sequence;

    @Column
    private Long depth;

    @Column
    private Long childCount;

    /* FK */

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;

    public String getContent(){
        if(this.isDeleted){
            return "삭제된 댓글입니다";
        }else{
            return this.content;
        }
    }

    private void setContent(String content){
        this.content = content;
    }

    void increaseChild(){
        this.childCount++;
    }

    void decreaseChild(){
        if(this.childCount > 0)
            this.childCount--;
    }

    void setParentComment(Comment parentComment, Long sequence){
        this.parentComment = parentComment;
        this.depth = this.parentComment.getDepth() + 1;
        this.groupId = parentComment.getGroupId();
        this.parentComment.increaseChild();
        this.sequence = sequence;
    }

    Comment(CommentDto commentDto){
        this.content = commentDto.getContent();
        this.creationDateTime = LocalDateTime.now();
        this.isDeleted = false;

        // 계층 정보
        this.depth = 0l;
        this.sequence = 0l;
        this.childCount = 0l;

        // 외래키 정보
        this.post = commentDto.getPost();
        this.member = commentDto.getWriter();
    }

    public boolean deleteComment(){
        this.isDeleted = true;
        if(this.childCount == 0){
            // 자식이 없으면 즉시 삭제
            return true;
        }else {
            // 자식이 있으면 계층 구조 유지를 위해 침묵
            return false;
        }
    }

    /**
     * 영속성 컨텍스트에 save되기 전 객체는 id를 가지고 있지 않기 때문에 생겨난 groupId 초기화 메서드
     * 대댓글인 경우 호출하지 말 것
     * 댓글 생성단계가 아니라면 호출하지 말 것
     * @param groupId
     */
    public void initializeGroupId(Long groupId){
        if(this.groupId == null){
            // 안전을 위해 null check 후 진행
            this.groupId = groupId;
        }
    }
    public static Comment createComment(CommentDto commentDto){
        return new Comment(commentDto);
    }

    public static void setParentComment(Comment comment, Comment parentComment, Long sequence){
        comment.setParentComment(parentComment, sequence);
    }

    public void updateComment(String content){
        setContent(content);
    }

    public static boolean deleteComment(Comment comment){
        boolean isDeleteInDB = comment.deleteComment();
        if(isDeleteInDB){
            if(comment.getParentComment() != null){
                // 부모 댓글로 가서 자식 댓글이 하나 감소하였음을 통지
                comment.getParentComment().decreaseChild();
            }
            return true;
        }else {
            return false;
        }
    }
}
