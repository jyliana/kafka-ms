package com.course.kafka.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private String name;

  @Column
  private Integer price;

  @Column
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
}
