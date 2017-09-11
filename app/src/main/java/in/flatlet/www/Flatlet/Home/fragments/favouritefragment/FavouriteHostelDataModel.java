package in.flatlet.www.Flatlet.Home.fragments.favouritefragment;


class FavouriteHostelDataModel {
    private String title;
    private String address_secondary;

    public float getFavouriteCardRating() {
        return favouriteCardRating;
    }

    public void setFavouriteCardRating(float favouriteCardRating) {
        this.favouriteCardRating = favouriteCardRating;
    }

    private float favouriteCardRating;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    private int rent;
    private double rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress_secondary() {
        return address_secondary;
    }

    public void setAddress_secondary(String address_secondary) {
        this.address_secondary = address_secondary;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


}
