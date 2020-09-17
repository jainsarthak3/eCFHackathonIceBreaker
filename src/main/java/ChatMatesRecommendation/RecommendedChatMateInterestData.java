package ChatMatesRecommendation;

import Database.ProfileData;

import java.util.Set;

public class RecommendedChatMateInterestData {
    private Integer recoDistance;
    private Set<String> interestsSet;
    private ProfileData profileData;

    public RecommendedChatMateInterestData(Integer recoDistance, Set<String> interestsSet, ProfileData profileData) {
        this.recoDistance = recoDistance;
        this.interestsSet = interestsSet;
        this.profileData = profileData;
    }

    public Integer getRecoDistance() {
        return recoDistance;
    }

    public void setRecoDistance(Integer recoDistance) {
        this.recoDistance = recoDistance;
    }

    public Set<String> getInterestsSet() {
        return interestsSet;
    }

    public void setInterestsSet(Set<String> interestsSet) {
        this.interestsSet = interestsSet;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }
}
