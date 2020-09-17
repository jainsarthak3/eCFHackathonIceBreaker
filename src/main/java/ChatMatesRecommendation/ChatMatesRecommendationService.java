package ChatMatesRecommendation;

import DAOLayer.DBDao;
import DAOLayer.DBDaoImpl;
import Database.ProfileData;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatMatesRecommendationService {
    private RecommendationEngine recommendationEngine = new RecommendationEngineImpl();
    DBDao dbDao = new DBDaoImpl();
    /**
     *
     * @param userProfile
     * @return
     */
    public List<RecommendedChatMateInterestData> fetchChatMateRecommendations(ProfileData userProfile, int filterSize) {
        List<RecommendedChatMateInterestData> recommendationList = new ArrayList<>();
        Map<String, ProfileData> userPoolData = dbDao.getUserPoolData();
        recommendationList = recommendationEngine.fetchRecommendations(userProfile, userPoolData);
        List<RecommendedChatMateInterestData> filteredRecommendationList = new ArrayList<>();
        return recommendationList;
    }
}
