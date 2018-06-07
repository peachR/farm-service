package com.yiyi.farm.entity.user;

import java.io.Serializable;

public class AuthorityEntitiy implements Serializable {
    private int id;
    private String description;

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

    @Override
    public String toString() {
        return "AuthorityEntitiy{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
