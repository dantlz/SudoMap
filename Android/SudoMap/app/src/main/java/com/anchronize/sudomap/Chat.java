package com.anchronize.sudomap;

/**
 * Created by tianlinz on 4/14/16.
 */
public class Chat {

    private String message;
    private String author;
    private String hour;
    private int votes;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    public Chat(String message, String author) {
        this.message = message;
        this.author = author;
        this.votes = 0;
    }

    public int getVotes() {
        return votes;
    }
    public void setHour(String hour){
        this.hour = hour;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getHour(){return hour;}
}