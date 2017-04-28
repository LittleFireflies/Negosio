package id.sch.smktelkom_mlg.projectwork.negosio.model;

/**
 * Created by LittleFireflies on 05-Feb-17.
 */

public class Booking {
    String tgl_booking;
    String product_name;
    String price;
    String start_date;
    String end_date;
    String time;
    String total;
    String buyer;
    String seller;
    String category;
    String img;
    String owner_token;
    String renter_token;
    String status;
    String buyer_phone;
    String buyer_location;
    String seller_phone;
    String seller_location;
    String reason;

    public String getTgl_booking() {
        return tgl_booking;
    }

    public void setTgl_booking(String tgl_booking) {
        this.tgl_booking = tgl_booking;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getBuyer_location() {
        return buyer_location;
    }

    public void setBuyer_location(String buyer_location) {
        this.buyer_location = buyer_location;
    }

    public String getOwner_token() {
        return owner_token;
    }

    public void setOwner_token(String owner_token) {
        this.owner_token = owner_token;
    }

    public String getRenter_token() {
        return renter_token;
    }

    public void setRenter_token(String renter_token) {
        this.renter_token = renter_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getSeller_location() {
        return seller_location;
    }

    public void setSeller_location(String seller_location) {
        this.seller_location = seller_location;
    }
}
