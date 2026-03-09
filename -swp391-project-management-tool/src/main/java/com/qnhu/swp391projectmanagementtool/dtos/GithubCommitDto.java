package com.qnhu.swp391projectmanagementtool.dtos;

import java.util.Map;

public class GithubCommitDto {
    private String sha;
    private CommitInfo commit;
    private Map<String, Object> author;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public CommitInfo getCommit() {
        return commit;
    }

    public void setCommit(CommitInfo commit) {
        this.commit = commit;
    }

    public Map<String, Object> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, Object> author) {
        this.author = author;
    }

    public static class CommitInfo {
        private Author author;
        private String message;

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Author {
        private String name;
        private String email;
        private String date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
