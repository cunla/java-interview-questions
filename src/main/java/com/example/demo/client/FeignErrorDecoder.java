package com.example.demo.client;

import com.example.demo.entity.HolidayError;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        HolidayError message;
        try (InputStream inputStream = response.body().asInputStream()) {
            message = mapper.readValue(inputStream, HolidayError.class);
            log.error("status is {} and error message is {} ", message.getStatus(), message.getError());
        } catch (Exception e) {
            return new Exception(e);
        }
        return errorDecoder.decode(s, response);
    }
}
