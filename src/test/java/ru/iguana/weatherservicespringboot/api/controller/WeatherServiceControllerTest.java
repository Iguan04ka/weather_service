package ru.iguana.weatherservicespringboot.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.iguana.weatherservicespringboot.api.dto.CityDto;
import ru.iguana.weatherservicespringboot.api.service.WeatherService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherServiceController.class)
class WeatherServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    @DisplayName("GET /weather/getForecast - success")
    void getForecast_shouldReturnCityDto() throws Exception {
        CityDto mockDto = new CityDto();
        mockDto.setName("Саратов");

        when(weatherService.getForecastByCityName("Саратов")).thenReturn(mockDto);

        mockMvc.perform(get("/weather/getForecast")
                        .param("cityName", "Саратов"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Саратов"));
    }

    @Test
    @DisplayName("POST /weather/saveForecast - success")
    void saveForecast_shouldCallServiceAndReturnOk() throws Exception {
        doNothing().when(weatherService).saveCityAndHisWeatherForecast("Москва", "2025-01-01");

        mockMvc.perform(post("/weather/saveForecast")
                        .param("cityName", "Москва")
                        .param("date", "2025-01-01"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /weather/deleteCity - success")
    void deleteCity_shouldCallServiceAndReturnOk() throws Exception {
        doNothing().when(weatherService).deleteCityByName("Казань");

        mockMvc.perform(delete("/weather/deleteCity")
                        .param("cityName", "Казань"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /weather/updateForecast - success")
    void updateForecast_shouldCallServiceAndReturnOk() throws Exception {
        doNothing().when(weatherService).updateWeatherForecastByCityName("Екатеринбург", "2025-06-01");

        mockMvc.perform(patch("/weather/updateForecast")
                        .param("cityName", "Екатеринбург")
                        .param("date", "2025-06-01"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /weather/getForecast - validation fail")
    void getForecast_shouldFailValidation_whenCityNameEmpty() throws Exception {
        mockMvc.perform(get("/weather/getForecast")
                        .param("cityName", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /weather/saveForecast - validation fail")
    void saveForecast_shouldFailValidation_whenMissingParams() throws Exception {
        mockMvc.perform(post("/weather/saveForecast")
                        .param("cityName", ""))
                .andExpect(status().isBadRequest());
    }
}
