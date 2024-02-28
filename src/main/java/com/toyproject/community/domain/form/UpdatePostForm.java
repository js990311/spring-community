package com.toyproject.community.domain.form;

import com.toyproject.community.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.sql.Update;

@Data
public class UpdatePostForm {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long id;

    public UpdatePostForm(){

    }
    public UpdatePostForm(Post post){
        this.title = post.getTitle();
        this.content = post.getContent();
        this.id = post.getId();
    }
}
