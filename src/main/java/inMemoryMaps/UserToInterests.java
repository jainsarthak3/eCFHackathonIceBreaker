package inMemoryMaps;

import model.AddInterestRequest;
import model.FetchInterestsRequest;
import model.FetchInterestsResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserToInterests {
    private Map<String, Set<String>> userToInterests = new HashMap<>();

    public UserToInterests() {
        Set<String> abhaysb = new HashSet<>(Arrays.asList("hackathon", "memes", "anime"));
        Set<String> dmgupta = new HashSet<>(Arrays.asList("hackathon", "football"));
        userToInterests.put("abhaysb", abhaysb);
        userToInterests.put("dmgupta", dmgupta);
    }

    /**
     * User can add interests in their profile.
     * @param addInterestRequest
     */
    public void addInterest(final AddInterestRequest addInterestRequest) {
        String userName = addInterestRequest.getUserName();
        Set<String> interests = userToInterests.computeIfAbsent(userName, k -> new HashSet<>());
        interests.add(addInterestRequest.getInterest());
    }

    /**
     * Returns interests added by user for their profile.
     * @param fetchInterestsRequest
     * @return
     */
    public FetchInterestsResponse returnInterests(final FetchInterestsRequest fetchInterestsRequest) {
        String userName = fetchInterestsRequest.getUserName();
        if(!userToInterests.containsKey(userName)) {
            throw new IllegalArgumentException("User doesn't exist. Please add interests to user first.");
        }
        return FetchInterestsResponse.builder().interests(userToInterests.get(userName)).build();
    }

}
