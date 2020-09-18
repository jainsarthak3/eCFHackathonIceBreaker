package model;

import lombok.Builder;

import java.util.List;

@Builder
public class FetchPopularInterestsResponse {
    List<String> interests;
}
