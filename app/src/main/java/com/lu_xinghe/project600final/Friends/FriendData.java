package com.lu_xinghe.project600final.Friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendData {

    List<Map<String,?>> friendList;

    public List<Map<String, ?>> getfriendList() {
        return friendList;
    }

	public int find(String query){
		int index=-1;
		for (int i=0;i<this.getSize();i++){
			if(query.equals(this.getItem(i).get("userName"))){
				index=i;
				i=this.getSize();
			}
		}
		return index;
	}

	public void addItem(int position, HashMap item){
		friendList.add(position, createMovie((String) item.get("userName"), (String) item.get("email"), (String) item.get("major"), (String) item.get("status"),  (String) item.get("about"), (String) item.get("profileImageURL")));
	}

    public int getSize(){
        return friendList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < friendList.size()){
            return (HashMap) friendList.get(i);
        } else return null;
    }

	public void removeItem(int movieID){
		friendList.remove(movieID);
	}

    public FriendData(){
        /*String userName;
		String email;
		String major;
		String status;
		String about;
		String profileImageURL;*/
        friendList = new ArrayList<Map<String,?>>();

    }


    private HashMap createMovie(String userName, String email, String major, String status, String about, String profileImageURL) {
        HashMap friend = new HashMap();
		friend.put("userName",userName);
		friend.put("email", email);
		friend.put("major", major);
		friend.put("status", status);
		friend.put("about",about);
		friend.put("profileImageURL",profileImageURL);

        return friend;
    }
}
