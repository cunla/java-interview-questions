package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "holidays"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("holidays")
    private List<Holiday> holidays = null;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("holidays")
    public List<Holiday> getHolidays() {
        return holidays;
    }

    @JsonProperty("holidays")
    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }
}