package org.techtown.myapplication;

public class NotificationModel {
    public String to;
    public Notification notification = new Notification();

    public static class Notification {
        public String text;

        public String title;
    }
}
