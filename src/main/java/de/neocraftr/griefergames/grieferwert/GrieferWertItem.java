package de.neocraftr.griefergames.grieferwert;

public class GrieferWertItem {
    private String name;
    private String url;
    private String imgUrl;
    private String priceRange;

    public GrieferWertItem(String name, String url, String imgUrl, String priceRange) {
        this.name = name;
        this.url = url;
        this.imgUrl = imgUrl;
        this.priceRange = priceRange;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPriceRange() {
        return priceRange;
    }
}
