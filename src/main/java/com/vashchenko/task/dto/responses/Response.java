package com.vashchenko.task.dto.responses;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class Response {
    private Integer code = HttpStatus.OK.value();

    public Response(HttpStatus status) {
        this.code = status.value();
    }
}
