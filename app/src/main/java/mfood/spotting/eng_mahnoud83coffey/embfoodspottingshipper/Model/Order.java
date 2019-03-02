package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model;

public class Order {

    private String ProudactID;
    private String ProudactName;
    private String Quentity;
    private String Price;
    private String Discount;

    public Order() {
    }

    public Order(String proudactID, String proudactName, String quentity, String price, String discount) {
        ProudactID = proudactID;
        ProudactName = proudactName;
        Quentity = quentity;
        Price = price;
        Discount = discount;
    }

    public String getProudactID() {
        return ProudactID;
    }

    public void setProudactID(String proudactID) {
        ProudactID = proudactID;
    }

    public String getProudactName() {
        return ProudactName;
    }

    public void setProudactName(String proudactName) {
        ProudactName = proudactName;
    }

    public String getQuentity() {
        return Quentity;
    }

    public void setQuentity(String quentity) {
        Quentity = quentity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }


}