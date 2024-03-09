package com.toyproject.community.domain.role;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceRole {
    @EmbeddedId
    private ResourceRoleId resourceRoleId;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    private Role role;

    @MapsId("resourceId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    void setResourceRole(ResourceRoleId id, Resource resource, Role role){
        this.resourceRoleId = id;
        this.resource = resource;
        this.role = role;
    }

    public static ResourceRole createResourceRole(Resource resource, Role role){
        ResourceRole resourceRole = new ResourceRole();
        ResourceRoleId resourceRoleId = new ResourceRoleId(
                resource.getId(),
                role.getRoleName()
        );
        resourceRole.setResourceRole(resourceRoleId, resource, role);
        return resourceRole;
    }


    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class ResourceRoleId implements Serializable{
        private Long resourceId;
        @Enumerated(EnumType.STRING)
        private RoleName roleName;

        ResourceRoleId(Long resourceId, RoleName roleName){
            this.resourceId = resourceId;
            this.roleName = roleName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResourceRoleId that = (ResourceRoleId) o;
            return Objects.equals(getResourceId(), that.getResourceId()) && getRoleName() == that.getRoleName();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getResourceId(), getRoleName());
        }
    }
}
