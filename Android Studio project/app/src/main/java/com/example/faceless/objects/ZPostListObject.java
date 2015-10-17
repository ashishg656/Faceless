package com.example.faceless.objects;

import java.util.List;

/**
 * Created by praveen goel on 10/17/2015.
 */
public class ZPostListObject {

    List<ZPostObject> posts_array;

    Integer next_page;

    public List<ZPostObject> getPosts_array() {
        return posts_array;
    }

    public void setPosts_array(List<ZPostObject> posts_array) {
        this.posts_array = posts_array;
    }

    public Integer getNext_page() {
        return next_page;
    }

    public void setNext_page(Integer next_page) {
        this.next_page = next_page;
    }
}
