package org.techtown.myapplication;

public class UserGroups {
    private String uid;
    private String groupKey;
    private String groupName;

    public UserGroups(){}

    public UserGroups(String uid, String groupKey, String groupName) {
        this.uid = uid;
        this.groupKey = groupKey;
        this.groupName = groupName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
