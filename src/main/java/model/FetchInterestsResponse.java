package model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class FetchInterestsResponse {
    private Set<String> interests;
}
