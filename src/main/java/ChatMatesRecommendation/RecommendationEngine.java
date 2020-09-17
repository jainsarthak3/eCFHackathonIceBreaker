package ChatMatesRecommendation;

import Database.ProfileData;

import java.util.List;
import java.util.Map;

public interface RecommendationEngine {
    /**
     *
     * @param userProfileData
     * @param userPoolData
     * @return
     */
    List<RecommendedChatMateInterestData> fetchRecommendations(ProfileData userProfileData, Map<String, ProfileData> userPoolData);
}
