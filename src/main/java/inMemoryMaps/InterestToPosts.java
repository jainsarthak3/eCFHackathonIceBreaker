package inMemoryMaps;

import model.AddPostToInterestsRequest;
import model.DownvoteRequest;
import model.FetchPopularInterestsResponse;
import model.FetchPostsForInterestsRequest;
import model.FetchPostsForInterestsResponse;
import model.Post;
import model.UpvoteRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InterestToPosts {
    // Map of interest -> Posts
    public Map<String, List<Post>> interestToPosts = new HashMap<>();

    public InterestToPosts() {
        Post hackathonPost = Post.builder()
                .user("Abhay")
                .post("The goal of a hackathon is to create functioning software or hardware by the end of the event. Hackathons tend to have a specific focus, which can include the programming language used, the operating system, an application, an API, or the subject and the demographic group of the programmers.")
                .build();
        Post footballPost = Post.builder()
                .user("Sarthak")
                .post("Football is a family of team sports that involve, to varying degrees, kicking a ball to score a goal. Unqualified, the word football normally means the form of football that is the most popular where the word is used.")
                .build();
        Post memePost = Post.builder()
                .user("Deepak")
                .post("A meme is a concept or behavior that spreads from person to person. Examples of memes include beliefs, fashions, stories, and phrases. In previous generations, memes typically spread within local cultures or social groups. However, now that the Internet has created a global community, memes can span countries and cultures across the world. Memes that are propogated online are called Internet memes")
                .build();
        Post animePost = Post.builder()
                .user("Rizul")
                .post("Anime, sometimes called Japanimation, is hand-drawn and computer animation originating from Japan. Anime, which is a shortening of the English word animation, is used in Japan as a blanket term for all animated works, regardless of origin. Outside of Japan, the term is used either to refer to animated works produced in Japan, or to their common visual style, which has since been adopted by a minority of works produced in other countries.")
                .build();
        interestToPosts.put("hackathon", Arrays.asList(hackathonPost));
        interestToPosts.put("football", Arrays.asList(footballPost));
        interestToPosts.put("meme", Arrays.asList(memePost));
        interestToPosts.put("anime", Arrays.asList(animePost));
    }

    /**
     * Maps posts to their interest for new feed when user posts something.
     *
     * @param addPostToInterestsRequest
     */
    public void addPostToInterest(final AddPostToInterestsRequest addPostToInterestsRequest) {
        Post userPost = Post.builder()
                .post(addPostToInterestsRequest.getPost())
                .user(addPostToInterestsRequest.getUser())
                .upvotes(0)
                .downvotes(0)
                .updatedAt(new Date().getTime())
                .build();
        System.out.println("user post: " + userPost);
        List<String> interests = addPostToInterestsRequest.getInterests();
        System.out.println("Interests: " + interests);
        for (String interest : interests) {
            if (!interestToPosts.containsKey(interest))
                interestToPosts.put(interest, new ArrayList<>());

            ArrayList<Post> posts = new ArrayList<>(interestToPosts.get(interest));
            System.out.println("Posts: " + posts);
//            List<Post> posts = interestToPosts.computeIfAbsent(interest, k -> new ArrayList<>());
            posts.add(userPost);
            interestToPosts.put(interest, posts);

            System.out.println("Ending");
        }
    }

    /**
     * Search posts using interests / refresh news feed.
     *
     * @param fetchPostsForInterestsRequest
     * @return
     */
    public FetchPostsForInterestsResponse fetchPostsForInterests(final FetchPostsForInterestsRequest fetchPostsForInterestsRequest) {
        Set<Post> postsSet = new HashSet<>();
        List<String> interests = fetchPostsForInterestsRequest.getInterests();
        for (String interest : interests) {
            if (interestToPosts.containsKey(interest)) {
                postsSet.addAll(interestToPosts.get(interest));
            }
        }
        List<Post> posts = new ArrayList<>(postsSet);
//        posts.sort(Comparator.comparing(Post::getUpdatedAt).reversed());
        return FetchPostsForInterestsResponse.builder().Posts(posts).build();
    }

    /**
     * Upvote.
     *
     * @param upvoteRequest
     */
    public void upvote(final UpvoteRequest upvoteRequest) {
        List<Post> posts = interestToPosts.get(upvoteRequest.getInterest())
                .stream().filter(p -> p.getPost().equals(upvoteRequest.getPost())).collect(Collectors.toList());
        for (Post post : posts) {
            post.setUpvotes(post.getUpvotes() + 1);
        }
    }

    /**
     * Downvote.
     *
     * @param downvoteRequest
     */
    public void downvote(final DownvoteRequest downvoteRequest) {
        List<Post> posts = interestToPosts.get(downvoteRequest.getInterest())
                .stream().filter(p -> p.getPost().equals(downvoteRequest.getPost())).collect(Collectors.toList());
        for (Post post : posts) {
            post.setDownvotes(post.getDownvotes() + 1);
        }
    }

    /**
     * Fetches popular trends to be displayed in sidebar of news feed.
     */
    public FetchPopularInterestsResponse fetchPopularInterests() {
        Map<String, Integer> popularity = new HashMap<>();
        for (Map.Entry<String, List<Post>> entry : interestToPosts.entrySet()) {
            popularity.put(entry.getKey(), entry.getValue().size());
        }
        popularity = popularity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return FetchPopularInterestsResponse.builder().interests(new ArrayList<>(popularity.keySet())).build();
    }
}
