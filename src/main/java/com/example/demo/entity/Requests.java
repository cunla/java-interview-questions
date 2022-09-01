package com.example.demo.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "used",
        "available",
        "resets"
})
public class Requests {

    @JsonProperty("used")
    private Integer used;
    @JsonProperty("available")
    private Integer available;
    @JsonProperty("resets")
    private String resets;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("used")
    public Integer getUsed() {
        return used;
    }

    @JsonProperty("used")
    public void setUsed(Integer used) {
        this.used = used;
    }

    @JsonProperty("available")
    public Integer getAvailable() {
        return available;
    }

    @JsonProperty("available")
    public void setAvailable(Integer available) {
        this.available = available;
    }

    @JsonProperty("resets")
    public String getResets() {
        return resets;
    }

    @JsonProperty("resets")
    public void setResets(String resets) {
        this.resets = resets;
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
