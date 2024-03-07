package com.toyproject.community.domain.role;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "role")
    private Set<ResourceRole> resourceRoles;

    public Role(String roleName){
        this.roleName = roleName;
    }
}
