package com.example.nikhiljoshi.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by nikhiljoshi on 1/27/16.
 *     original title
 *     movie poster image thumbnail
 *     A plot synopsis (called overview in the api)
 *     user rating (called vote_average in the api)
 *     release date
 */
public class Movie implements Parcelable {

    private String originalTitle;
    private String posterImageUrl;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie(String originalTitle, String posterImageUrl, String plotSynopsis,
                 String userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterImageUrl = posterImageUrl;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    Movie(Parcel in) {
        this.originalTitle = in.readString();
        this.posterImageUrl = in.readString();
        this.plotSynopsis = in.readString();
        this.userRating = in.readString();
        this.releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterImageUrl);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }



}
