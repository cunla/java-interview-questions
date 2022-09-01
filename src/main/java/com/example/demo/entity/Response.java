package com.example.demo.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "warning",
        "requests",
        "holidays"
})
public class Response {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("warning")
    private String warning;
    @JsonProperty("requests")
    private Requests requests;
    @JsonProperty("holidays")
    private List<Holiday> holidays = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("warning")
    public String getWarning() {
        return warning;
    }

    @JsonProperty("warning")
    public void setWarning(String warning) {
        this.warning = warning;
    }

    @JsonProperty("requests")
    public Requests getRequests() {
        return requests;
    }

    @JsonProperty("requests")
    public void setRequests(Requests requests) {
        this.requests = requests;
    }

    @JsonProperty("holidays")
    public List<Holiday> getHolidays() {
        return holidays;
    }

    @JsonProperty("holidays")
    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
