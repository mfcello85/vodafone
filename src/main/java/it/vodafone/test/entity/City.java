package it.vodafone.test.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents the model of a city/country coming from the data imported from Istat
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "code", length = 10)
    private String code;
}
