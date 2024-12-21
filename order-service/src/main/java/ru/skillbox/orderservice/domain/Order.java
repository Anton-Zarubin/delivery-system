package ru.skillbox.orderservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderDetails> orderDetails = new ArrayList<>();


    @Column(name = "cost")
    private BigDecimal cost;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    public Order(
            String description,
            String departureAddress,
            String destinationAddress,
            List<OrderDetails> orderDetails,
            BigDecimal cost,
            OrderStatus status
    ) {
        this.description = description;
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.orderDetails = orderDetails;
        this.cost = cost;
        this.status = status;
    }

    public Order(
            String departureAddress,
            String destinationAddress,
            String description,
            BigDecimal cost,
            OrderStatus status
    ) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.description = description;
        this.cost = cost;
        this.status = status;
    }

    public void addOrderDetails(Map<Long, Integer> productQuantityMap) {
        List<OrderDetails> orderDetails = new ArrayList<>();
        productQuantityMap.forEach((key, value) ->
                orderDetails.add(new OrderDetails(null, this, key, value)));

        getOrderDetails().addAll(orderDetails);
    }

    public void addStatusHistory(OrderStatus status, ServiceName serviceName, String comment) {
        getOrderStatusHistory().add(new OrderStatusHistory(null, status, serviceName, comment, this));
    }
}
