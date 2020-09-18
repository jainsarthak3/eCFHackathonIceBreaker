package model;

import lombok.Data;

import java.util.List;

@Data
public class AddPostToInterestsRequest {
    private List<String> interests;
    private String post;
    private String user;
}
