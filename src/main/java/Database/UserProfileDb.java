package Database;

import java.util.HashMap;
import java.util.Map;

public class UserProfileDb {
    private Map<String, ProfileData> userProfileMap = new HashMap<>();

    public Map<String, ProfileData> getUserPoolData() {
        return userProfileMap;
    }

    public void populateUserPoolData(Map<String, ProfileData> userProfileMap) {
        this.userProfileMap = userProfileMap;
    }

    public ProfileData getUserProfileData(String profileId) {
        return userProfileMap.get(profileId);
    }
}
