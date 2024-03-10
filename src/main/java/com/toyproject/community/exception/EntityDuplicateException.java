package com.toyproject.community.exception;


import lombok.Getter;

@Getter
public class EntityDuplicateException extends Exception{
    private String duplicateColumn;

    public EntityDuplicateException(String duplicateColumn) {
        this.duplicateColumn = duplicateColumn;
    }
}
