package com.course.kafka.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue
  private int orderId;

  @Column
  private String orderNumber;

  @Column
  private String orderLocation;

  @Column
  private OffsetDateTime orderDateTime;

  @Column
  private String creditCardNumber;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems;
}
