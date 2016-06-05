package com.example.noura.movies;

/**
 * Created by rabab on 18/12/15.
 */
public class MovieItem  {


    //content array list
    private String title ;
    private String image;
    private String overview;
    private String vote_average;
    private String release_date;
    private int _id;
    private String backdrop_path;

    //to use it in review movie
    private String id;


private int icon;

    public int getIcon() {
        return icon;
    }


    public MovieItem() {

    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle (){

        return title;

    }

    public String getId(){
        return id;
    }
    public String getImage(){

        return image;

    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setTitle(String yourTitle){

       this.title=yourTitle;
    }

    public void setImage(String yourImage){
        this.image=yourImage;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    public void setIcon(int icon) {
        this.icon = icon;
    }
}
