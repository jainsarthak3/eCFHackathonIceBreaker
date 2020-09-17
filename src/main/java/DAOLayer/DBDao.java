package DAOLayer;

import Database.ProfileData;

import java.util.Map;

public interface DBDao {
    /**
     *
     * @return
     */
    Map<String, ProfileData> getUserPoolData();

    /**
     *
     * @return
     */
    ProfileData getUserData(String profileId);

    /**
     *
     * @param userProfileData
     */
    void saveUserProfileData(ProfileData userProfileData);

    /**
     *
     * @param userPoolData
     */
    void populateUserPoolData(Map<String, ProfileData> userPoolData);
}
