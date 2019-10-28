package com.lukefire.dummyevent;

public class Upload {

    private String mName;
    private String mImageUrl;

    public Upload(){
        //empty constructor needed
    }

    public Upload(String name,String event_url){
        if(name.trim().equals("")){
            name="No Name";
        }
        mName=name;
        mImageUrl=event_url;
    }
    public String getName(){
        return mName;
    }
    public void setNmae(String name){
        mName=name;
    }
    public String getImageUrl(){
        return mImageUrl;
    }
    public void setImageUrl(String event_url){
        mImageUrl=event_url;
    }
}
