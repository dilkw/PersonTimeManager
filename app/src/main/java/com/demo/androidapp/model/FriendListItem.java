package com.demo.androidapp.model;

import com.demo.androidapp.model.entity.User;

public class FriendListItem {

    private User user;

    private boolean is_friend;

    public FriendListItem(User user, boolean is_friend) {
        this.user = user;
        this.is_friend = is_friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsFriend() {
        return is_friend;
    }

    public void setIsFriend(boolean isFriend) {
        this.is_friend = isFriend;
    }

    @Override
    public String toString() {
        return "FriendListItem{" +
                "user=" + user.toString() +
                ", isFriend=" + is_friend +
                '}';
    }
}
