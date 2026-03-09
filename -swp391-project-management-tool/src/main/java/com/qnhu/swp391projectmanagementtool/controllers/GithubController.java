package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.GithubCommitDto;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/commits")
    public ResponseEntity<List<GithubCommitDto>> getCommits(
            @RequestParam String owner,
            @RequestParam String repo) {
        try {
            List<GithubCommitDto> commits = githubService.getRepositoryCommits(owner, repo);
            return ResponseEntity.ok(commits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
