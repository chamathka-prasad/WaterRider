package com.codebee.waterrider.service;


import com.codebee.waterrider.dto.AuthDTO;
import com.codebee.waterrider.dto.AuthResponseDTO;
import com.codebee.waterrider.dto.CustomerChatDto;
import com.codebee.waterrider.dto.OrderDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WaterAppService {

    //
//    @POST("customer/register")
//    Call<MessageDto> register(@Body RegisterDto customer);
//
//
//    @POST("city")
//    Call<List<City>> getCity();
//
    @POST("driver/login")
    Call<AuthResponseDTO> auth(@Body AuthDTO authDTO);

    //
//
//    @POST("customer/product")
//    Call<List<ProductDTO>> getProducts();
//
//
//    @POST("customer/cart/add")
//    Call<MessageDto> addToCart(@Body CartDto cartDto, @Header("Authorization") String token);
//
//    @POST("customer/cart/getCount")
//    Call<MessageDto> getItemCounts(@Body AuthDTO email, @Header("Authorization") String token);
//
//    @POST("customer/cart/getAllItems")
//    Call<List<CartItemsDto>> getCartItem(@Body AuthDTO email, @Header("Authorization") String token);
//
//    @POST("customer/order/add")
//    Call<MessageDto> addOrder(@Body LocationDto location, @Header("Authorization") String token);
//
//
    @POST("driver/order/get")
    Call<List<OrderDto>> getOrders(@Body AuthDTO email, @Header("Authorization") String token);

    @POST("driver/order/getcustomers")
    Call<List<CustomerChatDto>> getCustomers(@Body AuthDTO email, @Header("Authorization") String token);



    @POST("driver/order/getcustomers")
    Call<List<CustomerChatDto>> getAllCustomers(@Body AuthDTO auth,@Header("Authorization") String token);

}
