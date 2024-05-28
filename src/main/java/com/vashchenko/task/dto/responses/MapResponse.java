package com.vashchenko.task.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.HashMap;

@Getter
public class MapResponse extends Response{
    @JsonProperty("data")
    private HashMap<String, Object> data;

    public MapResponse(HttpStatus httpStatus) {
        super(httpStatus);
        this.data = new HashMap<>();
    }

    public MapResponse() {
        super();
        this.data = new HashMap<>();
    }

    public void addData(String key, Object value){
        data.put(key,value);
    }
}
