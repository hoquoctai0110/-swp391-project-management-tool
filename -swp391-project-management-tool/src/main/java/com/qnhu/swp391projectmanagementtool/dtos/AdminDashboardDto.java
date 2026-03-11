package com.qnhu.swp391projectmanagementtool.dtos;

public class AdminDashboardDto {

    private long totalProjects;
    private long totalIssues;

    private long todo;
    private long inProgress;
    private long done;

    private long totalMembers;
    private long totalLeaders;
    private long totalLecturers;

    public AdminDashboardDto() {
    }

    public AdminDashboardDto(long totalProjects,
                             long totalIssues,
                             long todo,
                             long inProgress,
                             long done,
                             long totalMembers,
                             long totalLeaders,
                             long totalLecturers) {

        this.totalProjects = totalProjects;
        this.totalIssues = totalIssues;
        this.todo = todo;
        this.inProgress = inProgress;
        this.done = done;
        this.totalMembers = totalMembers;
        this.totalLeaders = totalLeaders;
        this.totalLecturers = totalLecturers;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(long totalProjects) {
        this.totalProjects = totalProjects;
    }

    public long getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(long totalIssues) {
        this.totalIssues = totalIssues;
    }

    public long getTodo() {
        return todo;
    }

    public void setTodo(long todo) {
        this.todo = todo;
    }

    public long getInProgress() {
        return inProgress;
    }

    public void setInProgress(long inProgress) {
        this.inProgress = inProgress;
    }

    public long getDone() {
        return done;
    }

    public void setDone(long done) {
        this.done = done;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public long getTotalLeaders() {
        return totalLeaders;
    }

    public void setTotalLeaders(long totalLeaders) {
        this.totalLeaders = totalLeaders;
    }

    public long getTotalLecturers() {
        return totalLecturers;
    }

    public void setTotalLecturers(long totalLecturers) {
        this.totalLecturers = totalLecturers;
    }
}