package com.toyproject.community.domain.role;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource {
    @Id
    @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    @Column
    private String url;

    @OneToMany(mappedBy = "resource")
    private List<ResourceRole> resourceRoles;

    @Column(name = "order_num")
    private Integer orderNum;

    public Resource(String url, Integer orderNum){
        this.url = url;
        this.orderNum = orderNum;
    }
}
