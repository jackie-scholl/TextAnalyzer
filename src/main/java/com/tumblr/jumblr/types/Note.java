package com.tumblr.jumblr.types;

public class Note extends Resource {
    private Long timestamp;
    private String blog_name;
    private String blog_url;
    private String added_text;
    private Long post_id;
    private String type;
    
    /**
     * @return the type ("like" or "reblog")
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the timestamp
     */
    public Long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return the blog name
     */
    public String getBlogName() {
        return this.blog_name;
    }

    /**
     * @return the blog url
     */
    public String getBlogUrl() {
        return this.blog_url;
    }

    /**
     * @return the added text
     */
    public String getAddedText() {
        return this.added_text;
    }

    /**
     * @return the post id
     */
    public Long getPostId() {
        return this.post_id;
    }

    @Override
    public String toString() {
        return String.format("%-8s", this.type + ",")
                + (this.timestamp != null ? String.format("%tF %1$tr, ", this.timestamp*1000L) : "")
                + (this.blog_name != null ? this.blog_name + ", " : "")
                + (this.post_id != null ? this.post_id + ", " : "")
                + (this.added_text != null ? "added_text=" + this.added_text : "");
    }
    
    
}
