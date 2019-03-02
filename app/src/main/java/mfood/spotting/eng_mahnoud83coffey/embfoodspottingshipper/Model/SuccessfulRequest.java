package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model;

public class SuccessfulRequest
{
    //---Shipper-----//
    private String nameShipper;
    private String phoneShipper;
    private String dateRequestShipper;
    private String ratingClientforShipper;
    private String commentClientForShipper;
    //---Details Order-----//
    private String orderId;
    private String addressClient;
    private String paymentMode;
    private String totalPrice;
    private String stateOrder;
    private String dateRequestClient;
    private String clientName;
    private String clientPhone;

    public SuccessfulRequest() {
    }

    public SuccessfulRequest(String nameShipper, String phoneShipper, String dateRequestShipper, String ratingClientforShipper, String commentClientForShipper, String orderId, String addressClient, String paymentMode, String totalPrice, String stateOrder, String dateRequestClient, String clientName, String clientPhone) {
        this.nameShipper = nameShipper;
        this.phoneShipper = phoneShipper;
        this.dateRequestShipper = dateRequestShipper;
        this.ratingClientforShipper = ratingClientforShipper;
        this.commentClientForShipper = commentClientForShipper;
        this.orderId = orderId;
        this.addressClient = addressClient;
        this.paymentMode = paymentMode;
        this.totalPrice = totalPrice;
        this.stateOrder = stateOrder;
        this.dateRequestClient = dateRequestClient;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
    }

    public String getNameShipper() {
        return nameShipper;
    }

    public void setNameShipper(String nameShipper) {
        this.nameShipper = nameShipper;
    }

    public String getPhoneShipper() {
        return phoneShipper;
    }

    public void setPhoneShipper(String phoneShipper) {
        this.phoneShipper = phoneShipper;
    }

    public String getDateRequestShipper() {
        return dateRequestShipper;
    }

    public void setDateRequestShipper(String dateRequestShipper) {
        this.dateRequestShipper = dateRequestShipper;
    }

    public String getRatingClientforShipper() {
        return ratingClientforShipper;
    }

    public void setRatingClientforShipper(String ratingClientforShipper) {
        this.ratingClientforShipper = ratingClientforShipper;
    }

    public String getCommentClientForShipper() {
        return commentClientForShipper;
    }

    public void setCommentClientForShipper(String commentClientForShipper) {
        this.commentClientForShipper = commentClientForShipper;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddressClient() {
        return addressClient;
    }

    public void setAddressClient(String addressClient) {
        this.addressClient = addressClient;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStateOrder() {
        return stateOrder;
    }

    public void setStateOrder(String stateOrder) {
        this.stateOrder = stateOrder;
    }

    public String getDateRequestClient() {
        return dateRequestClient;
    }

    public void setDateRequestClient(String dateRequestClient) {
        this.dateRequestClient = dateRequestClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
