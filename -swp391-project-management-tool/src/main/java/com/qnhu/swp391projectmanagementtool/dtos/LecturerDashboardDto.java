package com.qnhu.swp391projectmanagementtool.dtos;

import lombok.Data;

@Data
public class LecturerDashboardDto {

    private long totalGroups;
    private long totalIssues;
    private long todo;
    private long inProgress;
    private long done;

}
