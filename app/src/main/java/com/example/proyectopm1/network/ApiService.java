package com.example.proyectopm1.network;

import android.text.BoringLayout;

import com.example.proyectopm1.models.Address;
import com.example.proyectopm1.models.AuthResponse;
import com.example.proyectopm1.models.Favorite;
import com.example.proyectopm1.models.Order;
import com.example.proyectopm1.models.PaymentIntentRequest;
import com.example.proyectopm1.models.PaymentIntentResponse;
import com.example.proyectopm1.models.Product;
import com.example.proyectopm1.models.Restaurant;
import com.example.proyectopm1.models.Review;
import com.example.proyectopm1.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body User user);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body Map<String, String> credentials);

    @GET("/api/users/profile")
    Call<User> getProfile();

    @PUT("/api/users/profile")
    Call<User> updateProfile(@Body User user);

    @PUT("/api/users/change-password")
    Call<Void> changePassword(@Body Map<String, String> body);

    @GET("/api/restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("/api/restaurants/{id}")
    Call<Restaurant> getRestaurant(@Path("id") String id);

    @POST("/api/restaurants")
    Call<Restaurant> createRestaurant(@Body Restaurant restaurant);

    @PUT("/api/restaurants/{id}")
    Call<Restaurant> updateRestaurant(@Path("id") String id, @Body Restaurant restaurant);

    @DELETE("/api/restaurants/{id}")
    Call<Void> deleteRestaurant(@Path("id") String id);

    @GET("/api/products/restaurant/{id}")
    Call<List<Product>> getProductsByRestaurant(@Path("id") String restaurantId);

    @POST("/api/products")
    Call<Product> createProduct(@Body Product product);

    @POST("/api/orders")
    Call<Order> createOrder(@Body Order order);

    @GET("/api/orders/my-orders")
    Call<List<Order>> getMyOrders();

    @GET("/api/orders/{id}")
    Call<Order> getOrder(@Path("id") String id);

    @PUT("/api/orders/{id}/status")
    Call<Order> updateOrderStatus(@Path("id") String id, @Body Map<String, String> status);

    @PUT("/api/orders/{id}/cancel")
    Call<Void> cancelOrder(@Path("id") String id);

    @GET("/api/delivery/available-orders")
    Call<List<Order>> getAvailableOrders();

    @PUT("/api/delivery/accept/{orderId}")
    Call<Order> acceptOrder(@Path("orderId") String orderId);

    @PUT("/api/delivery/update-location")
    Call<Void> updateLocation(@Body Map<String, Double> location);

    @PUT("/api/delivery/deliver/{orderId}")
    Call<Order> deliverOrder(@Path("orderId") String orderId);

    @POST("/api/payment/create-intent")
    Call<PaymentIntentResponse> createPaymentIntent(@Body PaymentIntentRequest request);

    @GET("/api/admin/users")
    Call<List<User>> getAllUsers();

    @PUT("/api/admin/users/{id}/role")
    Call<User> changeUserRole(@Path("id") String id, @Body Map<String, String> role);

    @GET("/api/admin/metrics")
    Call<BoringLayout.Metrics> getMetrics();

    @POST("/api/reviews")
    Call<Review> createReview(@Body Review review);

    @GET("/api/reviews/restaurant/{id}")
    Call<List<Review>> getReviewsByRestaurant(@Path("id") String restaurantId);

    @POST("/api/addresses")
    Call<Address> addAddress(@Body Address address);

    @GET("/api/addresses")
    Call<List<Address>> getAddresses();

    @POST("/api/favorites")
    Call<Favorite> addFavorite(@Body Favorite favorite);

    @GET("/api/favorites")
    Call<List<Favorite>> getFavorites();
}
