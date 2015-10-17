package com.example.faceless.objects;

import java.util.List;

public class ZAllChannelsObject {

    List<ChannelObject> channels;

    public class ChannelObject {
        String name;
        int id;
        boolean is_unsubscribed;
        String last_chat_msg;
        String last_chat_time;

        public String getLast_chat_msg() {
            return last_chat_msg;
        }

        public void setLast_chat_msg(String last_chat_msg) {
            this.last_chat_msg = last_chat_msg;
        }

        public String getLast_chat_time() {
            return last_chat_time;
        }

        public void setLast_chat_time(String last_chat_time) {
            this.last_chat_time = last_chat_time;
        }

        public boolean is_unsubscribed() {
            return is_unsubscribed;
        }

        public void setIs_unsubscribed(boolean is_unsubscribed) {
            this.is_unsubscribed = is_unsubscribed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public List<ChannelObject> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelObject> channels) {
        this.channels = channels;
    }
}
