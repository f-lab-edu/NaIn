package com.flab.recipebook.user.domain;

public class User {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public User(Builder builder) {
        this.username = builder.username;
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public static class Builder{
        private String username;
        private String nickname;
        private String password;
        private String email;
        private String role;

        public Builder() {
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }
        public Builder nickname(String nickname){
            this.nickname = nickname;
            return this;
        }
        public Builder password(String password){
            this.password = password;
            return this;
        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder role(UserRole role){
            this.role = role.name();
            return this;
        }

        public User build() {
            if (this.role == null) {
                this.role = UserRole.USER.name();
            }
            return new User(this);
        }
    }
}
