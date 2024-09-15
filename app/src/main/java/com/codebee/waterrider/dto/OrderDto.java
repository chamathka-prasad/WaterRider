package com.codebee.waterrider.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private Long id;

    private String firstName;
    private String lastName;

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String latitude;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCartItemsDtos(List<OrderItemDto> cartItemsDtos) {
        this.cartItemsDtos = cartItemsDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<OrderItemDto> getCartItemsDtos() {
        return cartItemsDtos;
    }

    public void setCartItemsDtos(OrderItemDto cartItemsDtos) {
        this.cartItemsDtos.add(cartItemsDtos);
    }

    private String longitude;

    private List<OrderItemDto> cartItemsDtos = new ArrayList<>();

}
