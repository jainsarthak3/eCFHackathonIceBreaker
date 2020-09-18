package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {
    private String post;
    private Long updatedAt;
    private Integer upvotes;
    private Integer downvotes;
    private String user;
}
