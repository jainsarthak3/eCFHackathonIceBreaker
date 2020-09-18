import ChatMatesRecommendation.ChatMatesRecommendationService;
import ChatMatesRecommendation.RecommendedChatMateInterestData;
import DAOLayer.DBDao;
import DAOLayer.DBDaoImpl;
import Database.ConnectionData;
import Database.InterestData;
import Database.ProfileData;
import Database.UserProfileDb;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import static j2html.TagCreator.*;

import com.google.gson.Gson;
import inMemoryMaps.InterestToPosts;
import inMemoryMaps.UserToInterests;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import model.*;
import org.json.JSONObject;
import spark.Spark;

import java.text.SimpleDateFormat;

import static spark.Spark.*;

public class IceBreakerStartChatActivity {
    private static DBDao dbDao = new DBDaoImpl();
    private static ChatMatesRecommendationService chatMatesRecommendationService = new ChatMatesRecommendationService();
    private static UserProfileDb userProfileDb;
    private static final int CHATMATES_FILTER_SIZE = 5;
    private static Map<WsContext, String> userUsernameMap = new ConcurrentHashMap<>();
    private static Map<String, WsContext> userToSession = new ConcurrentHashMap<>();
    private static Map<String, String> matchMap = new HashMap<>();
    public static void main(String[] args) {
        Spark.staticFiles.location("/public");
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

        //OpenChatWindow Starts here
        List<String> usernameStore = new ArrayList<>();
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public");
        }).start(7070);
        app.get("/match/:users", ctx -> {
            String users[] = ctx.pathParam("users").split(",");
            addUserMapping(users);
            String matchDetails = printMatch();
            ctx.result("User1 : " + users[0] + " User 2 " + users[1] + " map size: " + matchMap.size() + " " + matchDetails);
//            ctx.cookieStore("username", users[0]);
            usernameStore.add(users[0]);
            ctx.redirect("http://localhost:7070/onechat.html");
        });

        app.ws("/chat", ws -> {
            ws.onConnect(context -> {
                String username = usernameStore.get(usernameStore.size() - 1);
                userUsernameMap.put(context, username);
                userToSession.put(username, context);
                context.send(new JSONObject()
                        .put("userMessage", username + " joined the chat")
                        .toString()
                );
            });
            ws.onClose(context -> {
                String username = userUsernameMap.get(context);
                userUsernameMap.remove(context);
                userToSession.remove(username);
                context.send(new JSONObject()
                        .put("userMessage", username + " left the chat")
                        .toString()
                );
            });
            ws.onMessage(context -> {
                sendMessageTo(userUsernameMap.get(context), context.message(), userToSession.get(matchMap.get(userUsernameMap.get(context))));
            });
        });

        UserToInterests userToInterests = new UserToInterests();
        InterestToPosts interestToPosts = new InterestToPosts();

        get("/hello", (req, res) -> "hi");

        get("/test/:input", ((request, response) ->
                "Hey " + request.params("input")
        ));

        get("/home", ((request, response) ->
                "<meta http-equiv = \"refresh\" content = \"2; url = http://localhost:4567/feed.html\" />\n"
        ));

        //addInterest
//        get("/interest/add", (req, res) -> {

//        });

//        fetchPostsForInterests
        get("/feed/:interests", (req, res) -> {
            FetchPostsForInterestsRequest request = new FetchPostsForInterestsRequest();
//            return Arrays.asList(req.params("interests").split("_"));
            request.setInterests(Arrays.asList(req.params("interests").split(",")));
            return new Gson().toJson(interestToPosts.fetchPostsForInterests(request));
        });

//        fetchInterestsForUser
        get("/interest/fetch/:user", ((req, res) -> {
            FetchInterestsRequest request = new FetchInterestsRequest();
            request.setUserName(req.params("user"));
            FetchInterestsResponse response = userToInterests.returnInterests(request);
            return new Gson().toJson(response);
        }));

        get("/add/post/:input", (req, res) -> {
            AddPostToInterestsRequest request = new AddPostToInterestsRequest();
            List<String> input = Arrays.asList(req.params("input").split("_"));
            request.setUser(input.get(0));
            request.setInterests(Collections.singletonList(input.get(1)));
            request.setPost(input.get(2));
            interestToPosts.addPostToInterest(request);
//            return new Gson().toJson(interestToPosts.interestToPosts);
            return  "<meta http-equiv = \"refresh\" content = \"2; url = http://localhost:4567/home\" />\n";
        });

        get("/add/interest", ((req, res) ->
                "<meta http-equiv = \"refresh\" content = \"2; url = http://localhost:4567/addUserInterest.html\" />\n"
        ));

//        });
        get("/add/post", ((req, res) ->
                "<meta http-equiv = \"refresh\" content = \"2; url = http://localhost:4567/addPost.html\" />\n"
        ));

//        addInterest
        get("/interest/add/:input", (req, res) -> {
            AddInterestRequest request = new AddInterestRequest();
            List<String> input = Arrays.asList(req.params("input").split(","));
            request.setUserName(input.get(0));
            request.setInterest(input.get(1));
            userToInterests.addInterest(request);
            res.redirect("/home");
            return new Gson().toJson(userToInterests);
        });
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

        List<ConnectionData> swConnectionList = new ArrayList<>();
        InterestData swInt1 = new InterestData("Matrimonial", "");
        InterestData swInt2 = new InterestData("Design", "");
        InterestData swInt3 = new InterestData("God", "");
        List<InterestData> swInterestDataList = new ArrayList<>();
        swInterestDataList.add(swInt1);
        swInterestDataList.add(swInt2);
        swInterestDataList.add(swInt3);
        ProfileData saryuwProfileData = new ProfileData("saryuw", "sw123", swConnectionList, swInterestDataList);

        List<ConnectionData> shwetankConnectionList = new ArrayList<>();
        InterestData shwetankInt1 = new InterestData("Cricket", "");
        InterestData shwetankInt2 = new InterestData("Coding", "");
        InterestData shwetankInt3 = new InterestData("Design", "");
        List<InterestData> shwetankInterestDataList = new ArrayList<>();
        shwetankInterestDataList.add(shwetankInt1);
        shwetankInterestDataList.add(shwetankInt2);
        shwetankInterestDataList.add(shwetankInt3);
        ProfileData shwetankProfileData = new ProfileData("shwetank", "shwetank123", shwetankConnectionList, shwetankInterestDataList);

        List<ConnectionData> abhishekConnectionList = new ArrayList<>();
        InterestData abhishekInt1 = new InterestData("Mathematics", "");
        InterestData abhishekInt2 = new InterestData("Design", "");
        InterestData abhishekInt3 = new InterestData("Cricket", "");
        List<InterestData> abhishekInterestDataList = new ArrayList<>();
        abhishekInterestDataList.add(abhishekInt1);
        abhishekInterestDataList.add(abhishekInt2);
        abhishekInterestDataList.add(abhishekInt3);
        ProfileData abhishekProfileData = new ProfileData("abhishek", "abhishek123", abhishekConnectionList, abhishekInterestDataList);

        userProfileMap.put("dmg123", deepakProfileData);
        userProfileMap.put("ab123", abhayProfileData);
        userProfileMap.put("riz123", rizulgProfileData);
        userProfileMap.put("sw123", saryuwProfileData);
        userProfileMap.put("shwetank123", shwetankProfileData);
        userProfileMap.put("abhishek123", abhishekProfileData);

        return userProfileMap;
    }

    private static String printMatch() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> match : matchMap.entrySet()) {
            res.append(match.getKey() + " matches to " + match.getValue() + "<br>");
        }
        return res.toString();
    }

    public static void addUserMapping(String[] users) {
        if (matchMap.containsKey(users[0]) || matchMap.containsKey(users[1])) {
            matchMap.remove(users[0]);
            matchMap.remove(users[1]);
        }
        matchMap.put(users[0], users[1]);
        matchMap.put(users[1], users[0]);
    }

    private static void sendMessageTo(String sender, String message, WsContext receiver) {
        if (receiver.session.isOpen() && userToSession.get(sender).session.isOpen()) {
            receiver.send(new JSONObject()
                    .put("userMessage", createHtmlMessage(sender, message))
                    .toString()
            );
            userToSession.get(sender).send(new JSONObject()
                    .put("userMessage", createHtmlMessage(sender, message))
                    .toString()
            );
        }
    }

    private static String createHtmlMessage(String sender, String message) {
        return article(
                b(sender + ":"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }
}