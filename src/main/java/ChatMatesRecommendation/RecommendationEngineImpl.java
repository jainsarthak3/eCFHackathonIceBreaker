package ChatMatesRecommendation;

import Database.InterestData;
import Database.ProfileData;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngineImpl implements RecommendationEngine {

    @Override
    public List<RecommendedChatMateInterestData> fetchRecommendations(ProfileData userProfileData, Map<String, ProfileData> userPoolData) {
        Set<String> userInterestsSet = userProfileData.getInterestsList()
                .stream().map(InterestData::getInterest)
                .collect(Collectors.toSet());
        List<RecommendedChatMateInterestData> recommendedChatMateInterestDataList = new ArrayList<>();
        userPoolData.forEach((candidateUserProfileId, candidateProfileData) -> {
            RecommendedChatMateInterestData candidateRecommendedChatMateInterestData =
                    getRecommendedChatMateInterestData(userInterestsSet, candidateProfileData);
            recommendedChatMateInterestDataList.add(candidateRecommendedChatMateInterestData);
        });
        return sortRecommendationChatMateInterestDataList(recommendedChatMateInterestDataList);
    }

    @NonNull
    private RecommendedChatMateInterestData getRecommendedChatMateInterestData(Set<String> userInterestsSet, ProfileData candidateProfileData) {
        Set<String> candidateInterestsSet = candidateProfileData.getInterestsList()
                .stream().map(InterestData::getInterest)
                .collect(Collectors.toSet());
        Set<String> interestsIntersectionSet = new HashSet<>(userInterestsSet);
        interestsIntersectionSet.retainAll(candidateInterestsSet);
        int recoDistance = interestsIntersectionSet.size();
        RecommendedChatMateInterestData candidateRecommendedChatMateInterestData = new RecommendedChatMateInterestData(
                recoDistance, interestsIntersectionSet, candidateProfileData);
        return candidateRecommendedChatMateInterestData;
    }

    private List<RecommendedChatMateInterestData> sortRecommendationChatMateInterestDataList(List<RecommendedChatMateInterestData> recommendedChatMateInterestDataList) {
        Collections.sort(recommendedChatMateInterestDataList, this::customCompare);
        return recommendedChatMateInterestDataList;
    }

    private int customCompare(RecommendedChatMateInterestData r1, RecommendedChatMateInterestData r2) {
        return r2.getRecoDistance() - r1.getRecoDistance();
    }
}
