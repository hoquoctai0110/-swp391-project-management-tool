package com.qnhu.swp391projectmanagementtool.dtos;

import lombok.Data;

@Data
public class GroupProgressDto {

    private long todo;
    private long inProgress;
    private long done;

}