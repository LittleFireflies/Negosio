package id.sch.smktelkom_mlg.projectwork.negosio.model;

/**
 * Created by Dwi Enggar on 24/01/2017.
 */

public class Barang {
    public String productname;
    public int price;
    public String description;
    public int category;
    public int type;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
