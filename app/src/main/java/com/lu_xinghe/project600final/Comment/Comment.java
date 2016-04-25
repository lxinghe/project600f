package com.lu_xinghe.project600final.Comment;

/**
 * Created by Lu,Xinghe on 2/14/2016.
 */

import java.io.Serializable;

public class Comment implements Serializable{
    String userName, comment, date;

    public Comment() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate(){return date;}

    public void setDate(String date){this.date=date;}

}
