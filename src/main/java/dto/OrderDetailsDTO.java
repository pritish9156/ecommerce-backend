package dto;

import java.util.List;

import entity.Order;
import entity.OrderItem;

public class OrderDetailsDTO {

    private Order order;

    private List<OrderItem> items;

    public OrderDetailsDTO() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(
            List<OrderItem> items) {
        this.items = items;
    }
}