package ru.iguana.weatherservicespringboot.data.repository.impl;

import org.springframework.stereotype.Repository;
import ru.iguana.weatherservicespringboot.data.model.City;
import ru.iguana.weatherservicespringboot.data.model.Weather;
import ru.iguana.weatherservicespringboot.data.repository.CityRepository;
import ru.iguana.weatherservicespringboot.data.repository.ConnectionData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class CityRepositoryImpl implements CityRepository {
    private final ConnectionData connectionData;

    public CityRepositoryImpl(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }

    @Override
    public Collection<City> findAll() {
        final String sql = "SELECT c.name, w.temperature, w.humidity, w.wind_speed " +
                "FROM city c LEFT JOIN weather w ON c.id = w.city_id " +
                "WHERE w.id IS NULL OR w.measured_at = (" +
                "    SELECT MAX(measured_at) FROM weather WHERE city_id = c.id" +
                ")";
        return getCitiesFromQuery(sql);
    }

    @Override
    public Optional<City> findOneByName(String name) {
        final String sql = "SELECT c.name, w.temperature, w.humidity, w.wind_speed " +
                "FROM city c LEFT JOIN weather w ON c.id = w.city_id " +
                "WHERE c.name = ? " +
                "ORDER BY w.measured_at DESC LIMIT 1";
        return getCityFromQuery(sql, name);
    }

    @Override
    public void save(City city) {
        Connection conn = null;
        try {
            conn = getConnection(connectionData);
            conn.setAutoCommit(false);

            int cityId = getOrCreateCityId(conn, city.getCityName());
            if (city.getWeather() != null) {
                insertWeatherData(conn, cityId, city.getWeather());
            }

            conn.commit();
        } catch (SQLException e) {
            rollbackTransaction(conn);
            throw new RuntimeException("Failed to save city and weather", e);
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    public void delete(City city) {
        final String sql = "DELETE FROM city WHERE name = ?";
        executeUpdate(sql, city.getCityName());
    }


    private int getOrCreateCityId(Connection conn, String cityName) throws SQLException {
        Integer cityId = findCityIdByName(conn, cityName);
        if (cityId == null) {
            cityId = insertCity(conn, cityName);
        }
        return cityId;
    }

    private Integer findCityIdByName(Connection conn, String cityName) throws SQLException {
        String sql = "SELECT id FROM city WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        }
    }

    private int insertCity(Connection conn, String cityName) throws SQLException {
        String sql = "INSERT INTO city (name) VALUES (?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new SQLException("Failed to insert city");
            }
        }
    }

    private void insertWeatherData(Connection conn, int cityId, Weather weather) throws SQLException {
        String sql = "INSERT INTO weather " +
                "(city_id, temperature, humidity, wind_speed, measured_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cityId);
            stmt.setInt(2, weather.getTemperature());
            stmt.setInt(3, weather.getHumidity());
            stmt.setInt(4, weather.getWindSpeed());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
    }

    private Collection<City> getCitiesFromQuery(String sql) {
        Collection<City> cities = new ArrayList<>();
        try (Connection conn = getConnection(connectionData);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cities.add(createCityFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch cities", e);
        }
        return cities;
    }

    private Optional<City> getCityFromQuery(String sql, String name) {
        try (Connection conn = getConnection(connectionData);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(createCityFromResultSet(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch city: " + name, e);
        }
    }

    private void executeUpdate(String sql, String param) {
        try (Connection conn = getConnection(connectionData);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, param);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed", e);
        }
    }


    private City createCityFromResultSet(ResultSet rs) throws SQLException {
        City city = new City(rs.getString("name"));
        if (rs.getObject("temperature") != null) {
            Weather weather = new Weather();
            weather.setTemperature(rs.getInt("temperature"));
            weather.setHumidity(rs.getInt("humidity"));
            weather.setWindSpeed(rs.getInt("wind_speed"));
            city.setWeather(weather);
        }
        return city;
    }

    private void rollbackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private Connection getConnection(ConnectionData connectionData) throws SQLException {
        return DriverManager.getConnection(connectionData.getUrl(), connectionData.getUser(), connectionData.getPassword());
    }
}

