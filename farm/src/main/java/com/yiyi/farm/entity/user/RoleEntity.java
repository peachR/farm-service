package com.yiyi.farm.entity.user;

import java.util.List;

public class RoleEntity {
    private int id;
    private String description;
    private List<AuthorityEntitiy> authorities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AuthorityEntitiy> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityEntitiy> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
