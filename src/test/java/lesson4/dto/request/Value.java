package lesson4.dto.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ingredients"
})
@Data
@Generated("jsonschema2pojo")
public class Value {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "ingredients"
    })

    @JsonProperty("ingredients")
    private List<Ingredient> ingredients = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        }

