package main.service;

import main.api.request.PostRequest;
import main.api.response.ApiPostListResponse;
import main.api.response.ApiPostResponse;
import main.api.response.AuthUserResponse;
import main.model.Post;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApiPostService {
    @Autowired
    private PostRepository postRepository;

    public ApiPostListResponse getPosts(int offset, int limit, String mode) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        List<ApiPostResponse> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset, limit);
        //Pageable pageable = new PostRequest(limit, offset);

        List<Post> postsFromRep = new ArrayList<>();
        switch (mode) {
            case ("recent") :
                Page<Post> page = postRepository.findRecentPosts(pageable);
                postsFromRep.addAll(page.getContent());

                //postsFromRep.addAll(postRepository.findRecentPosts(pageable).getContent());
                 break;
            case ("best") :
                postsFromRep = postRepository.findBestPosts(pageable).getContent();
                break;
            case ("popular") :
                postsFromRep = postRepository.findPopularPosts(pageable).getContent();
                break;
            case ("early") :
                postsFromRep = postRepository.findEarlyPosts(pageable).getContent();

                break;
        }

        for (Post p : postsFromRep) {
            int postId = p.getId();
            long timestamp = p.getTime().getTime() / 1000;
            int likeCount = postRepository.findPostLikesCount(postId);
            int dislikeCount = postRepository.findPostDislikesCount(postId);
            int commentCount = postRepository.findPostCommentsCount(postId);
            int viewCount = p.getViewCount();

            AuthUserResponse user = new AuthUserResponse();
            user.setId(p.getUser().getId());
            user.setName(p.getUser().getName());

            posts.add(new ApiPostResponse(postId, timestamp, user, p.getTitle(), p.getText(), likeCount, dislikeCount, commentCount, viewCount));
        }

        apiList.setPosts(posts);
        apiList.setCount(postRepository.findActivePostsCount());
        return apiList;
    }
}
