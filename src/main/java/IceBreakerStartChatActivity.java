import ChatMatesRecommendation.ChatMatesRecommendationService;
import ChatMatesRecommendation.RecommendedChatMateInterestData;
import DAOLayer.DBDao;
import DAOLayer.DBDaoImpl;
import Database.ConnectionData;
import Database.InterestData;
import Database.ProfileData;
import Database.UserProfileDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static spark.Spark.*;

public class IceBreakerStartChatActivity {
    private static DBDao dbDao = new DBDaoImpl();
    private static ChatMatesRecommendationService chatMatesRecommendationService = new ChatMatesRecommendationService();
    private static UserProfileDb userProfileDb;
    private static final int CHATMATES_FILTER_SIZE = 5;
    public static void main(String[] args) {
        staticFileLocation("/public");
        dbDao.populateUserPoolData(populateUserPoolData());
        final List<RecommendedChatMateInterestData>[] recommendedChatMateInterestDataList = new List[]{new ArrayList<>()};
        AtomicReference<String> userProfileId = new AtomicReference<>("");
        get("/startChat/route/:id", (req, res) -> {
            res.type("application/json");
            ProfileData userProfile = dbDao.getUserData(req.params(":id"));
            userProfileId.set(userProfile.getProfileId());
            recommendedChatMateInterestDataList[0] = chatMatesRecommendationService.fetchChatMateRecommendations(userProfile, CHATMATES_FILTER_SIZE);
            res.redirect("/startChat/routes");
            return "";
        });

        get("/startChat/routes", (request, response) -> UILayer.getHtmlPage(recommendedChatMateInterestDataList[0], userProfileId.get()));
    }

    //TODO: replace here with generic dao
    private static Map<String, ProfileData> populateUserPoolData(){
        Map<String, ProfileData> userProfileMap = new HashMap<>();
        List<ConnectionData> rizulgConnectionList = new ArrayList<>();
        InterestData rgInt1 = new InterestData("Football", "");
        InterestData rgInt2 = new InterestData("Coding", "");
        InterestData rgInt3 = new InterestData("God", "");
        List<InterestData> rgInterestDataList = new ArrayList<>();
        rgInterestDataList.add(rgInt1);
        rgInterestDataList.add(rgInt2);
        rgInterestDataList.add(rgInt3);
        ProfileData rizulgProfileData = new ProfileData("rizulJI", "riz123", rizulgConnectionList, rgInterestDataList);

        List<ConnectionData> abhayConnectionList = new ArrayList<>();
        InterestData abInt1 = new InterestData("Matrimonial", "");
        InterestData abInt2 = new InterestData("Design", "");
        InterestData abInt3 = new InterestData("Logical Reasoning", "");
        List<InterestData> abInterestDataList = new ArrayList<>();
        abInterestDataList.add(abInt1);
        abInterestDataList.add(abInt2);
        abInterestDataList.add(abInt3);
        ProfileData abhayProfileData = new ProfileData("abhay", "ab123", abhayConnectionList, abInterestDataList);

        List<ConnectionData> dmgConnectionList = new ArrayList<>();
        InterestData dmgInt1 = new InterestData("Mathematics", "");
        InterestData dmgInt2 = new InterestData("Coding", "");
        InterestData dmgInt3 = new InterestData("God", "");
        List<InterestData> dmgInterestDataList = new ArrayList<>();
        dmgInterestDataList.add(dmgInt1);
        dmgInterestDataList.add(dmgInt2);
        dmgInterestDataList.add(dmgInt3);
        ProfileData deepakProfileData = new ProfileData("deepak", "dmg123", dmgConnectionList, dmgInterestDataList);

        userProfileMap.put("dmg123", deepakProfileData);
        userProfileMap.put("ab123", abhayProfileData);
        userProfileMap.put("riz123", rizulgProfileData);
        return userProfileMap;
    }
}