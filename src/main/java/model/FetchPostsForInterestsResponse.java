package model;

import lombok.Builder;

import java.util.List;

@Builder
public class FetchPostsForInterestsResponse {
    List<Post> Posts;
}
