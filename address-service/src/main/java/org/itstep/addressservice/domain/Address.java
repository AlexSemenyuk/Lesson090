package org.itstep.addressservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@JsonProperty("count")
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "country", nullable = false, length = 50)
    private String country;
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "city", nullable = false, length = 50)
    private String city;
    @Column(name = "address_line1", nullable = false, length = 100)
    @NotBlank
    private String addressLine1;
    @Column(name = "address_line2", length = 100)
    private String addressLine2;
}
