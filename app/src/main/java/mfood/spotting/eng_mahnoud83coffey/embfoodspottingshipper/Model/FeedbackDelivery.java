package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model;

public class FeedbackDelivery
{

    private String shipperName;
    private String ratingShipper;
    private String idOrder;
    private String idOrderShipper;


    public FeedbackDelivery() {
    }

    public FeedbackDelivery(String shipperName, String ratingShipper, String idOrder, String idOrderShipper) {
        this.shipperName = shipperName;
        this.ratingShipper = ratingShipper;
        this.idOrder = idOrder;
        this.idOrderShipper = idOrderShipper;
    }


    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getRatingShipper() {
        return ratingShipper;
    }

    public void setRatingShipper(String ratingShipper) {
        this.ratingShipper = ratingShipper;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdOrderShipper() {
        return idOrderShipper;
    }

    public void setIdOrderShipper(String idOrderShipper) {
        this.idOrderShipper = idOrderShipper;
    }
}
