package ru.iguana.weatherservicespringboot.data.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.iguana.weatherservicespringboot.data.model.City;
import ru.iguana.weatherservicespringboot.data.model.Weather;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public class CityRepositoryImpl implements CityRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<City> cityRowMapper = (rs, rowNum) -> {
        City city = new City(rs.getString("name"));
        if (rs.getObject("temperature") != null) {
            Weather weather = new Weather();
            weather.setTemperature(rs.getInt("temperature"));
            weather.setHumidity(rs.getInt("humidity"));
            weather.setWindSpeed(rs.getInt("wind_speed"));
            city.setWeather(weather);
        }
        return city;
    };

    public CityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<City> findAll() {
        final String sql = "SELECT c.name, w.temperature, w.humidity, w.wind_speed " +
                "FROM city c LEFT JOIN weather w ON c.id = w.city_id " +
                "WHERE w.id IS NULL OR w.measured_at = (" +
                "    SELECT MAX(measured_at) FROM weather WHERE city_id = c.id" +
                ")";
        return jdbcTemplate.query(sql, cityRowMapper);
    }

    @Override
    public Optional<City> findOneByName(String name) {
        final String sql = "SELECT c.name, w.temperature, w.humidity, w.wind_speed " +
                "FROM city c LEFT JOIN weather w ON c.id = w.city_id " +
                "WHERE c.name = ? " +
                "ORDER BY w.measured_at DESC LIMIT 1";
        return jdbcTemplate.query(sql, cityRowMapper, name)
                .stream()
                .findFirst();
    }

    @Override
    public void save(City city) {
        int cityId = getOrCreateCityId(city.getCityName());
        if (city.getWeather() != null) {
            insertWeatherData(cityId, city.getWeather());
        }
    }

    @Override
    public void delete(City city) {
        final String sql = "DELETE FROM city WHERE name = ?";
        jdbcTemplate.update(sql, city.getCityName());
    }

    private int getOrCreateCityId(String cityName) {
        Integer cityId = findCityIdByName(cityName);
        if (cityId == null) {
            cityId = insertCity(cityName);
        }
        return cityId;
    }

    private Integer findCityIdByName(String cityName) {
        final String sql = "SELECT id FROM city WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, cityName);
    }

    private int insertCity(String cityName) {
        final String sql = "INSERT INTO city (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cityName);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private void insertWeatherData(int cityId, Weather weather) {
        final String sql = "INSERT INTO weather " +
                "(city_id, temperature, humidity, wind_speed, measured_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                cityId,
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getWindSpeed(),
                Timestamp.valueOf(LocalDateTime.now()));
    }
}