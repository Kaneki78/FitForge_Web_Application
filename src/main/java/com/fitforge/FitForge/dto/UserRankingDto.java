package com.fitforge.FitForge.dto;

public class UserRankingDto {

    private String username;
    private String profilePictureFilename;
    private Long workoutCount;
    private long rank;

    public UserRankingDto() {
    }

    public UserRankingDto(String username, String profilePictureFilename, Long workoutCount) {
        this.username = username;
        this.profilePictureFilename = profilePictureFilename;
        this.workoutCount = workoutCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureFilename() {
        return profilePictureFilename;
    }

    public void setProfilePictureFilename(String profilePictureFilename) {
        this.profilePictureFilename = profilePictureFilename;
    }

    public Long getWorkoutCount() {
        return workoutCount;
    }

    public void setWorkoutCount(Long workoutCount) {
        this.workoutCount = workoutCount;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}