package ru.iguana.weatherservicespringboot.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Entity
@Table(
        schema = "public",
        name = "city"
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_id", referencedColumnName = "id")
    private WeatherEntity weather;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;
}
