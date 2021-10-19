package main.service;

import lombok.AllArgsConstructor;
import main.api.response.ApiPostListResponse;
import main.dto.PostDto;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApiPostService {
    private final PostRepository postRepository;
    private final MapperService mapperService;

    public ApiPostListResponse getPosts(int offset, int limit, String mode) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        List<Post> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset, limit);
        switch (mode) {
            case "recent":
                Page<Post> recentPage = postRepository.findRecentPosts(pageable);
                posts.addAll(recentPage.getContent());
                apiList.setCount(recentPage.getTotalElements());
                break;
            case "popular":
                Page<Post> popularPage = postRepository.findPopularPosts(pageable);
                posts.addAll(popularPage.getContent());
                apiList.setCount(popularPage.getTotalElements());
                break;
            case "early":
                Page<Post> earlyPage = postRepository.findEarlyPosts(pageable);
                posts.addAll(earlyPage.getContent());
                apiList.setCount(earlyPage.getTotalElements());
                break;
            case "best":
                Page<Post> bestPage = postRepository.findBestPosts(pageable);
                posts.addAll(bestPage.getContent());
                apiList.setCount(bestPage.getTotalElements());
                break;
            default :
                apiList.setCount(0);
        }
        List<PostDto> postDtoList = posts.stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList());
        apiList.setPosts(postDtoList);
        return apiList;
    }
}
