package org.example.model;

public class Player {
    private Roles roleType;
    private Team teamType;
    private User[] users;

    public Player(Roles roleType, Team teamType, User[] users) {
        this.roleType = roleType;
        this.teamType = teamType;
        this.users = users;
    }

    // Getters and setters for roleType, teamType, and users
    public Roles getRoleType() {
        return roleType;
    }

    public void setRoleType(Roles roleType) {
        this.roleType = roleType;
    }

    public Team getTeamType() {
        return teamType;
    }

    public void setTeamType(Team teamType) {
        this.teamType = teamType;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}

