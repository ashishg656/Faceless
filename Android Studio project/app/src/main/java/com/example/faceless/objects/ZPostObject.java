package com.example.faceless.objects;

/**
 * Created by praveen goel on 10/17/2015.
 */
public class ZPostObject {

    boolean is_poll;
    String heading;
    String description;
    String image;
    String date;
    int id;
    int comment_count;
    Integer upvotes_count;
    int has_downvoted;
    int has_upvoted;

    public boolean is_poll() {
        return is_poll;
    }

    public void setIs_poll(boolean is_poll) {
        this.is_poll = is_poll;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public Integer getUpvotes_count() {
        return upvotes_count;
    }

    public void setUpvotes_count(Integer upvotes_count) {
        this.upvotes_count = upvotes_count;
    }

    public int getHas_downvoted() {
        return has_downvoted;
    }

    public void setHas_downvoted(int has_downvoted) {
        this.has_downvoted = has_downvoted;
    }

    public int getHas_upvoted() {
        return has_upvoted;
    }

    public void setHas_upvoted(int has_upvoted) {
        this.has_upvoted = has_upvoted;
    }
}
