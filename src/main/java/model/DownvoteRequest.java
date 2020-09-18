package model;

import lombok.Data;

@Data
public class DownvoteRequest {
    private String interest;
    private String post;
}
