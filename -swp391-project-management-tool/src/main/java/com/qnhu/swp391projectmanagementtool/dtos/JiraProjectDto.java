package com.qnhu.swp391projectmanagementtool.dtos;

public class JiraProjectDto {

    private String id;
    private String key;
    private String name;

    public JiraProjectDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}