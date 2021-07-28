package org.techtown.myapplication;

public class Group {
    private String name;
    private String key;
    private int numberofMember;

    public Group(){}

    public Group(String name, String key, int numberofMember) {
        this.name = name;
        this.key = key;
        this.numberofMember = numberofMember;
    }

    public int getNumberofMember() {
        return numberofMember;
    }

    public void setNumberofMember(int numberofMember) {
        this.numberofMember = numberofMember;
    }

    public void groupExit(){
        this.numberofMember--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
