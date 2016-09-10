package com.xuzhihui.picsart;


/**
 * Created by Jav-Xu on 16/9/4.
 */
public class Image {

    private String name;
    private int srcId;
    private String url;

    public Image(String name, int srcId, String url) {
        this.name = name;
        this.srcId = srcId;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
