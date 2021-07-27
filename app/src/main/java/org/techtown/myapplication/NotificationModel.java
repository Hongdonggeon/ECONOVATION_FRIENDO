package org.techtown.myapplication;

public class NotificationModel {
    public Object to;
    public Notification notification = new Notification();
    public Data data = new Data();

    public static class Notification {
        public String text;
        public String title;
    }

    public static class Data{
        public String title;
        public String text;
    }
}
