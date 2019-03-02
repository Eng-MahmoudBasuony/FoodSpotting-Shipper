package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model;

public class ShippingInformation
{

    private String orderId,shipperPhone;
    private double lat,lng;

    public ShippingInformation() {
    }

    public ShippingInformation(String orderId, String shipperPhone, double lat, double lng) {
        this.orderId = orderId;
        this.shipperPhone = shipperPhone;
        this.lat = lat;
        this.lng = lng;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
