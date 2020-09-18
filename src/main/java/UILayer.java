import ChatMatesRecommendation.RecommendedChatMateInterestData;
import DAOLayer.DBDao;
import DAOLayer.DBDaoImpl;
import Database.InterestData;
import Database.ProfileData;

import java.util.List;
import java.util.Set;

public final class UILayer {
    private static DBDao dbDao = new DBDaoImpl();
    private UILayer(List<RecommendedChatMateInterestData> recommendedChatMateInterestDataList) {}

    public static final String getHtmlPage(List<RecommendedChatMateInterestData> recommendedChatMateInterestDataList, String userProfileId) {
        String header = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <link href='https://fonts.googleapis.com/css?family=Oswald:300' rel='stylesheet' type='text/css'>\n" +
                "    <style> * {\n" +
                "        -webkit-transition-property: all;\n" +
                "        -webkit-transition-duration: .2s;\n" +
                "        -moz-transition-timing-function: cubic-bezier(100,50,21,6);\n" +
                "        -moz-transition-property: all;\n" +
                "        -moz-transition-timing-function: cubic-bezier(100,50,21,6);\n" +
                "    }\n" +
                "\n" +
                "    body{\n" +
                "        background: crimson;\n" +
                "        padding:75px;\n" +
                "        text-align:center;\n" +
                "        font-family: 'Oswald', sans-serif;\n" +
                "    }\n" +
                "\n" +
                "    h1{\n" +
                "        color:#1b1b2b;\n" +
                "        font-weight:300;\n" +
                "    }\n" +
                "\n" +
                "    h2{\n" +
                "        color:#1b1b2b;\n" +
                "        font-weight:50;\n" +
                "    }\n" +
                "\n" +
                "    h3{\n" +
                "        color: black;\n" +
                "        font-weight:25\n" +
                "    }\n" +
                "\n" +
                "    h4{\n" +
                "        color: white;\n" +
                "        font-weight:50\n" +
                "    }\n" +
                "\n" +
                "    .btn{\n" +
                "        color:#999;\n" +
                "        background:rgba(0, 0, 0, 5);\n" +
                "        padding:10px 20px;\n" +
                "        font-size:12px;\n" +
                "        text-decoration:none;\n" +
                "        letter-spacing:2px;\n" +
                "        text-transform:uppercase;\n" +
                "    }\n" +
                "\n" +
                "    .btn:hover{\n" +
                "        border:none;\n" +
                "        background:rgba(0, 0, 0, 0.4);\n" +
                "        background:#fff;\n" +
                "        padding:20px 20px; #000;\n" +
                "        color:#1b1b1b;\n" +
                "    }\n" +
                "\n" +
                "    .footer{\n" +
                "        font-size:8px;\n" +
                "        color:#fff;\n" +
                "        clear:both;\n" +
                "        display:block;\n" +
                "        letter-spacing:5px;\n" +
                "        border:1px solid #fff;\n" +
                "        padding:5px;\n" +
                "        text-decoration:none;\n" +
                "        width:210px;\n" +
                "        margin:auto;\n" +
                "        margin-top:400px;\n" +
                "    } </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<script src=\"startChatJS.js\"></script>\n" +
                "<h1> Hello " + dbDao.getUserData(userProfileId).getProfileUsername() + "! " +
                "Let's do some Ice-Breakin' shall we?</h1>\n" +
                "<h2> Connect with chat-mates having your matching interests </h2>\n" +
                "<br>";
        int count = 0;
        for(int i=0; i<recommendedChatMateInterestDataList.size(); i++){
            RecommendedChatMateInterestData recommendedChatMateInterestData = recommendedChatMateInterestDataList.get(i);
            Set<String> interestsSet = recommendedChatMateInterestData.getInterestsSet();
            String chatMateProfileId = recommendedChatMateInterestData.getProfileData().getProfileId();
            if(interestsSet.size()==0 || recommendedChatMateInterestData.getProfileData().getProfileId()==userProfileId){
                continue;
            }
            String profileUsername = recommendedChatMateInterestData.getProfileData().getProfileUsername();
            count++;
            header = header + "<a href=\"#\" class=\"btn\" onclick=\"window.location.href = 'http://localhost:7070/match/" + userProfileId + "," + chatMateProfileId +  "';\">" + "Start chat with " + profileUsername + "</a> <h3> Common Interests </h3> <h4>"+ interestsSet + "</h4>" + "<br>";
        }
        if(count==0){
            header = header + "<h4> Sorry, no available users are matching with your profile. Would you like to tune into more interests? :D </h4>";
        }
        String footer = "</body>\n" +
                "</html>";
        return header + footer;
    }
}
