package com.example.demo.client;

import com.example.demo.entity.HolidayError;
import com.example.demo.exception.HolidayApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        HolidayError message;
        try (InputStream inputStream = response.body().asInputStream()) {
            message = mapper.readValue(inputStream, HolidayError.class);
            log.error("status is {} and error message is {} ", message.getStatus(), message.getError());
            return new HolidayApiException(message);
        } catch (Exception e) {
            return new Exception(e);
        }
    }
}
