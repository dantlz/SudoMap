package com.anchronize.sudomap.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tianlinz on 3/25/16.
 */
public class Event implements Serializable {

    public static final long serialVersionUID = 1L;

    public Event(){

    }

    public Event(String ID){
        eventID = ID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    public String getEventID(){
        return eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public ArrayList<User> getAttendants() {
        return attendants;
    }

    public void addAttendant(User attendant) {
        this.attendants.add(attendant);
    }

    public void removeAttendant(String attendantID){
        for(User atdnt: attendants){
            if(atdnt.getUserID().equals(attendantID)){
                attendants.remove(atdnt);
            }
        }
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(String postID){
        for(Post post: posts){
            if(post.getPostID().equals(postID)){
                posts.remove(post);
            }
        }
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public HashMap<Integer, Integer> getnumOfPostsAtTime() {
        return numOfPostsAtTime;
    }

    public String getcreationTime() {
        return creationTime;
    }

    private String eventID;
    private String title;
    private String description;
    private String category;
    private String organizerID;
    private ArrayList<User> attendants = new ArrayList<User>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private boolean privacy;
    private String address;
    private double longitude;
    private double latitude;
    private boolean visible;
    private HashMap<Integer, Integer> numOfPostsAtTime = new HashMap<Integer, Integer>();
    private String creationTime;
}
