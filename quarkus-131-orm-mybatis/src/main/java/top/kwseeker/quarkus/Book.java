package top.kwseeker.quarkus;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
public class Book implements Serializable {

    private Integer id;
    private User author;
    private String title;

}
