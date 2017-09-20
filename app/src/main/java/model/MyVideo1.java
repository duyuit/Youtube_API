package model;

import java.io.Serializable;

/**
 * Created by Vu Khac Hoi on 9/17/2017.
 */

public class MyVideo1 implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public MyVideo1(String title, String thumnail, String id, String channelTitle) {

        this.title = title;
        this.thumnail = thumnail;
        this.id = id;
        this.channelTitle = channelTitle;
    }

    private String thumnail;
    private String id;
    private  String channelTitle;
}
