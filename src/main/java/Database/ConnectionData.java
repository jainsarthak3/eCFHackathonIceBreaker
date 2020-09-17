package Database;

import java.util.List;

public class ConnectionData {
    private String profileId;
    private ProfileData connectionProfile;
    private String connectionProfileMetadata;

    public ConnectionData(ProfileData connectionProfile, String connectionProfileMetadata) {
        this.profileId = connectionProfile.getProfileId();
        this.connectionProfile = connectionProfile;
        this.connectionProfileMetadata = connectionProfileMetadata;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public ProfileData getConnectionProfile() {
        return connectionProfile;
    }

    public void setConnectionProfile(ProfileData connectionProfile) {
        this.connectionProfile = connectionProfile;
    }

    public String getConnectionProfileMetadata() {
        return connectionProfileMetadata;
    }

    public void setConnectionProfileMetadata(String connectionProfileMetadata) {
        this.connectionProfileMetadata = connectionProfileMetadata;
    }
}
