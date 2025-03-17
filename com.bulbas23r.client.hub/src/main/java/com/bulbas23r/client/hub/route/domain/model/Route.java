package com.bulbas23r.client.hub.route.domain.model;

import common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted is false")
@Table(name = "p_route")
public class Route extends BaseEntity {

    @EmbeddedId
    private RouteId id;

    @Column(nullable = false)
    private Integer transitTime;

    @Column(nullable = false)
    private Integer transitDistance;
}
