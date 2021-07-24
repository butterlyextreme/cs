package org.task.data.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Table(name = "order_book")
@Entity(name = "order_book")
public class OrderBookEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )

    private UUID id;

    @Column(name = "instrument_id")
    private String instrumentId;
    private String state;


    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.JOIN)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<OrdersEntity> ordersEntities = new HashSet<>();

}
