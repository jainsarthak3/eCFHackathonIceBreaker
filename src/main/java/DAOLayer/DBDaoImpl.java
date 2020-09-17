package DAOLayer;

import Database.ProfileData;
import Database.UserProfileDb;

import java.util.Map;

public class DBDaoImpl implements DBDao {
    private static UserProfileDb userProfileDb = new UserProfileDb();

    @Override
    public Map<String, ProfileData> getUserPoolData() {
        return userProfileDb.getUserPoolData();
    }

    @Override
    public ProfileData getUserData(String profileId) {
        return userProfileDb.getUserProfileData(profileId);
    }

    @Override
    public void saveUserProfileData(ProfileData userProfileData) {

    }

    @Override
    public void populateUserPoolData(Map<String, ProfileData> userPoolData) {
        userProfileDb.populateUserPoolData(userPoolData);
    }
}
