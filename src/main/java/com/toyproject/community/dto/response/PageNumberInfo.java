package com.toyproject.community.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageNumberInfo {
    private int nowIndex;
    private List<Integer> pageNumbers;
    private int lastIndex;

    public PageNumberInfo(int nowIndex, int lastIndex) {
        pageNumbers = new ArrayList<>();
        for(int i=nowIndex-2; pageNumbers.size() <= 5; i++){
            if(i<0){ // 페이지번호가 -일수는 없으므로 제외
                continue;
            }else if(i>lastIndex){ // 최대 페이지보다 페이지 수가 많으면 제외
                break;
            }
            pageNumbers.add(i);
        }
        this.nowIndex = nowIndex;
        this.lastIndex = lastIndex;
    }
}
