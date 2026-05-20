package ru.nobody.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRestResponse {

    private RestStatus status = new RestStatus();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestStatus {
        private ResultEnum result = ResultEnum.OK;
        private String text = null;
    }

    public enum ResultEnum {
        OK,
        ERROR
    }
}
