package model;

import lombok.Data;

@Data
public class UpvoteRequest {
    private String interest;
    private String post;
}
