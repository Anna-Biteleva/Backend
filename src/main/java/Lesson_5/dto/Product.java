package Lesson_5.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@With
@Data
public class Product {
    private Integer id;
    private String title;
    private Integer price;
    private String categoryTitle;

}
