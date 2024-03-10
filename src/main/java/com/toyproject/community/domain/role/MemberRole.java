package com.toyproject.community.domain.role;

import com.toyproject.community.domain.Board;
import com.toyproject.community.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRole {
    @EmbeddedId
    private MemberRoleId memberRoleId;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    private Role role;

    public MemberRole(Member member, Role role) {
        this.member = member;
        this.role = role;
    }

    void setMemberRole(MemberRoleId id, Member member, Role role){
        this.memberRoleId = id;
        this.member = member;
        this.role = role;
    }

    public static MemberRole createMemberRole(Member member, Role role){
        MemberRole memberRole = new MemberRole();
        MemberRoleId resourceRoleId = new MemberRoleId(
                member.getId(),
                role.getRoleName()
        );
        memberRole.setMemberRole(resourceRoleId, member, role);
        return memberRole;
    }


    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class MemberRoleId implements Serializable{
        private Long memberId;
        @Enumerated(EnumType.STRING)
        private RoleName roleName;

        MemberRoleId(Long memberId, RoleName roleName){
            this.memberId = memberId;
            this.roleName = roleName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MemberRoleId that = (MemberRoleId) o;
            return Objects.equals(memberId, that.memberId) && roleName == that.roleName;
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberId, roleName);
        }
    }
}

