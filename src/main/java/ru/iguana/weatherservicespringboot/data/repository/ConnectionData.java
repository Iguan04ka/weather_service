package ru.iguana.weatherservicespringboot.data.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ConnectionData {
    private final String url;
    private final String user;
    private final String password;

    public ConnectionData() {
        this.url = "jdbc:postgresql://localhost:5433/Weather";
        this.user = "iguana";
        this.password = "postgres";
    }
}

