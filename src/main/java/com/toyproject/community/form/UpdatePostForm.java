package com.toyproject.community.form;

import com.toyproject.community.domain.Post;
import lombok.Data;
import org.hibernate.sql.Update;

@Data
public class UpdatePostForm {
    private String title;
    private String content;
    private Long id;

    public UpdatePostForm(){

    }
    public UpdatePostForm(Post post){
        this.title = post.getTitle();
        this.content = post.getContent();
        this.id = post.getId();
    }
}
