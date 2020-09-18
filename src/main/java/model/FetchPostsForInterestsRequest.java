package model;

import lombok.Data;

import java.util.List;

@Data
public class FetchPostsForInterestsRequest {
    private List<String> interests;
}
