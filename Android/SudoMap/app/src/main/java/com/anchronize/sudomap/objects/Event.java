package com.anchronize.sudomap.objects;


        import android.location.Location;

        import java.sql.Timestamp;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Vector;

/**
 * Created by tianlinz on 3/25/16.
 */
public class Event extends SudoMarker{

    public Event(String ID){
        eventID = ID;
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

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<User> getAttendants() {
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

    public List<Post> getPosts() {
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

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public Timestamp getcreationTime() {
        return creationTime;
    }

    private String eventID;
    private String title;
    private String description;
    private String category;
    private User organizer;
    private List<User> attendants;
    private List<Post> posts;
    private String privacy;
    private Location location;
    private boolean visible;
    private HashMap<Integer, Integer> numOfPostsAtTime;
    private Timestamp creationTime;
}
