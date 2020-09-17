package Database;

import java.util.List;

public class ProfileData {
    private String profileUsername;
    private String profileId;
    private List<ConnectionData>  connectionsList;
    private List<InterestData> interestsList;

    public ProfileData(String profileUsername, String profileId, List<ConnectionData> connectionsList, List<InterestData> interestsList) {
        this.profileUsername = profileUsername;
        this.profileId = profileId;
        this.connectionsList = connectionsList;
        this.interestsList = interestsList;
    }

    public String getProfileUsername() {
        return profileUsername;
    }

    public void setProfileUsername(String profileUsername) {
        this.profileUsername = profileUsername;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public List<ConnectionData> getConnectionsList() {
        return connectionsList;
    }

    public void setConnectionsList(List<ConnectionData> connectionsList) {
        this.connectionsList = connectionsList;
    }

    public List<InterestData> getInterestsList() {
        return interestsList;
    }

    public void setInterestsList(List<InterestData> interestsList) {
        this.interestsList = interestsList;
    }
}
