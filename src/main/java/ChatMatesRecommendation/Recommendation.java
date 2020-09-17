package ChatMatesRecommendation;

import Database.InterestData;
import Database.ProfileData;

import java.util.Set;

public class Recommendation {
    private ProfileData recommendedProfile;
    private Set<InterestData> matchingInterestsSet;

    public Recommendation(ProfileData recommendedProfile, Set<InterestData> matchingInterestsSet) {
        this.recommendedProfile = recommendedProfile;
        this.matchingInterestsSet = matchingInterestsSet;
    }

    public ProfileData getRecommendedProfile() {
        return recommendedProfile;
    }

    public void setRecommendedProfile(ProfileData recommendedProfile) {
        this.recommendedProfile = recommendedProfile;
    }

    public Set<InterestData> getMatchingInterestsSet() {
        return matchingInterestsSet;
    }

    public void setMatchingInterestsSet(Set<InterestData> matchingInterestsSet) {
        this.matchingInterestsSet = matchingInterestsSet;
    }
}
