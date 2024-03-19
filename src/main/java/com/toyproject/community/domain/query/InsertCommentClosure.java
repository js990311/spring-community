package com.toyproject.community.domain.query;

import lombok.Getter;

@Getter
public class InsertCommentClosure {
    private Long ancestorId;
    private Long descendantId;
    private Long depth;

    public InsertCommentClosure(Long ancestorId, Long descendantId, Long depth) {
        this.ancestorId = ancestorId;
        this.descendantId = descendantId;
        this.depth = depth;
    }
}
