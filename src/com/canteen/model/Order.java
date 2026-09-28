package com.canteen.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private String id;
    private String orderNumber;
    private String pickupToken;
    private String userId;
    private String studentName;
    private String studentRollNo;
    private String studentPhone;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private String paymentMethod;
    private String notes;

    public Order() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.orderTime = LocalDateTime.now();
        this.paymentMethod = "Pay at Counter";
    }

    public Order(String id, String orderNumber, String pickupToken, String userId, String studentName, String studentRollNo, String studentPhone, List<OrderItem> items, double totalAmount, String paymentMethod, String notes) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.pickupToken = pickupToken;
        this.userId = userId;
        this.studentName = studentName;
        this.studentRollNo = studentRollNo;
        this.studentPhone = studentPhone;
        this.items = items != null ? items : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PLACED;
        this.orderTime = LocalDateTime.now();
        this.paymentMethod = paymentMethod != null ? paymentMethod : "Pay at Counter";
        this.notes = notes != null ? notes : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPickupToken() {
        return pickupToken;
    }

    public void setPickupToken(String pickupToken) {
        this.pickupToken = pickupToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalItemCount() {
        int count = 0;
        if (items != null) {
            for (OrderItem item : items) {
                count += item.getQuantity();
            }
        }
        return count;
    }

    public String getFormattedTotal() {
        return String.format("₹%.2f", totalAmount);
    }

    public String getFormattedOrderTime() {
        if (orderTime == null) return "";
        return orderTime.format(FORMATTER);
    }
}
