package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Model;

import java.util.List;

public class Request
{
    private String phoneClient;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<Order> foods; //List of food Order
    private String latlng;
    private String comment;
    private String paymentMethod;
    private String paymentState;
    private String phoneShipper;


    public Request() {
    }

    public Request(String phoneClient, String name, String address, String total, String status, List<Order> foods, String latlng, String comment, String paymentMethod, String paymentState, String phoneShipper) {
        this.phoneClient = phoneClient;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.foods = foods;
        this.latlng = latlng;
        this.comment = comment;
        this.paymentMethod = paymentMethod;
        this.paymentState = paymentState;
        this.phoneShipper = phoneShipper;
    }

    public String getPhoneClient() {
        return phoneClient;
    }

    public void setPhoneClient(String phoneClient) {
        this.phoneClient = phoneClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getPhoneShipper() {
        return phoneShipper;
    }

    public void setPhoneShipper(String phoneShipper) {
        this.phoneShipper = phoneShipper;
    }


}