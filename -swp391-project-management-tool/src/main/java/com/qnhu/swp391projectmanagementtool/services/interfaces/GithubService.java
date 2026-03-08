package com.qnhu.swp391projectmanagementtool.services.interfaces;

import com.qnhu.swp391projectmanagementtool.dtos.GithubCommitDto;
import java.util.List;

public interface GithubService {
    List<GithubCommitDto> getRepositoryCommits(String owner, String repo);
}
