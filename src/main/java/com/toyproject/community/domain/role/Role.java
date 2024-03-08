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
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_name", unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_role")
    private Role parentRole;

    @OneToMany(mappedBy = "role")
    private List<MemberRole> memberRoles;

    @OneToMany(mappedBy = "role")
    private Set<ResourceRole> resourceRoles;

    public Role(RoleName roleName){
        this.roleName = roleName;
    }
}
