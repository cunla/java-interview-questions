package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "error"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayError {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error")
    private String error;

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }
}
