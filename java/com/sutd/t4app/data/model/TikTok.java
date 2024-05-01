package com.sutd.t4app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TikTok extends RealmObject implements Parcelable {
    @PrimaryKey @Required private ObjectId _id;
    private String name;
    private String img;
    private String link;
    private String RestaurantId;

    public TikTok(){}


    protected TikTok(Parcel in){
        img= in.readString();
        link=in.readString();
        name=in.readString();
        RestaurantId=in.readString();

    }

    public static final Creator<TikTok> CREATOR= new Creator<TikTok>() {
        @Override
        public TikTok createFromParcel(Parcel in) {
            return new TikTok(in);
        }

        @Override
        public TikTok[] newArray(int size) {
            return new TikTok[size];
        }
    };
    public String getRestaurantId(){return this.RestaurantId;}

    public String getNameTikTok(){ return this.name;}
    public String getImg(){return this.img;}
    public String getLink(){return this.link;}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(link);
        dest.writeString(name);
        dest.writeString(RestaurantId);

    }
}
