package main.service;

import main.api.response.ApiPostListResponse;
import main.dto.PostDto;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiPostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MapperService mapperService;

    public ApiPostListResponse getPosts(int offset, int limit, String mode) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        List<Post> posts = getListPost(offset, limit, mode);
        List<PostDto> postDtoList = posts.stream().map(p -> mapperService.convertPostToDto(p))
                .collect(Collectors.toList());
        apiList.setPosts(postDtoList);
        apiList.setCount(postDtoList.size());
        return apiList;
    }

    public List<Post> getListPost(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset, limit);
        switch (mode) {
            case "recent":
                return postRepository.findRecentPosts(pageable).getContent();
            case "popular":
                return postRepository.findPopularPosts(pageable).getContent();
            case "early":
                return postRepository.findEarlyPosts(pageable).getContent();
            case "best":
                return postRepository.findBestPosts(pageable).getContent();
        }
        return new ArrayList<>();
    }
}
