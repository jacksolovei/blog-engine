package main.service;

import lombok.AllArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.*;
import main.dto.CommentDto;
import main.dto.PostDto;
import main.dto.UserDto;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApiPostService {
    public static final String RECENT_MODE = "recent";
    public static final int MIN_TITLE_LENGTH = 3;
    public static final int MIN_TEXT_LENGTH = 50;

    private final PostRepository postRepository;
    private final MapperService mapperService;
    private final AuthCheckService authCheckService;
    private final UserRepository userRepository;

    public ApiPostListResponse getPosts(int offset, int limit, String mode) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        List<Post> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page;
        switch (mode) {
            case "popular":
                page = postRepository.findPopularPosts(pageable);
                break;
            case "early":
                page = postRepository.findEarlyPosts(pageable);
                break;
            case "best":
                page = postRepository.findBestPosts(pageable);
                break;
            default:
                page = postRepository.findRecentPosts(pageable);
        }
        posts.addAll(page.getContent());
        apiList.setCount(page.getTotalElements());
        List<PostDto> postDtoList = posts.stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList());
        apiList.setPosts(postDtoList);
        return apiList;
    }

    public ApiPostListResponse getPostsByQuery(int offset, int limit, String query) {
        if (query == null || query.matches("\\s+")) {
            return getPosts(offset, limit, RECENT_MODE);
        }
        ApiPostListResponse apiList = new ApiPostListResponse();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page = postRepository.findPostsByQuery(pageable, query);
        apiList.setPosts(page.getContent().stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList()));
        apiList.setCount(page.getTotalElements());
        return apiList;
    }

    public ApiPostListResponse getPostsByDate(int offset, int limit, String date) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page = postRepository.findPostsByDate(pageable, date);
        apiList.setPosts(page.getContent().stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList()));
        apiList.setCount(page.getTotalElements());
        return apiList;
    }

    public ApiPostListResponse getPostsByTag(int offset, int limit, String tag) {
        ApiPostListResponse apiList = new ApiPostListResponse();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page = postRepository.findPostsByTag(pageable, tag);
        apiList.setPosts(page.getContent().stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList()));
        apiList.setCount(page.getTotalElements());
        return apiList;
    }

    public Optional<Post> getPostById(int id) {
        return postRepository.findActivePostById(id);
    }

    public PostByIdResponse getPostResponseById(Post post, Principal principal) {
        AuthCheckResponse authCheckResponse = authCheckService.getAuthCheck(principal);
        if (authCheckResponse.isResult()) {
            UserDto user = authCheckResponse.getUser();
            if (!user.isModeration() || user.getId() != post.getUser().getId()) {
                updateViewCount(post);
            }
        } else {
            updateViewCount(post);
        }
        List<CommentDto> comments = post.getPostComments().stream()
                .map(mapperService::convertCommentToDto)
                .collect(Collectors.toList());
        List<String> tags = post.getTags().stream().map(Tag::getName)
                .collect(Collectors.toList());
        PostDto postDto = mapperService.convertPostToDto(post);

        return new PostByIdResponse(postDto.getId(), postDto.getTimestamp(),
                true, postDto.getUser(), postDto.getTitle(), postDto.getText(),
                postDto.getLikeCount(), postDto.getDislikeCount(), postDto.getViewCount(),
                comments, tags);
    }

    private void updateViewCount(Post post) {
        int view = post.getViewCount();
        post.setViewCount(view + 1);
        postRepository.save(post);
    }

    public ApiPostListResponse getPostsByStatus(int offset, int limit, String status, Principal principal) {
        String email = principal.getName();
        ApiPostListResponse apiPostListResponse = new ApiPostListResponse();
        List<Post> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page;
        switch (status) {
            case "inactive":
                page = postRepository.findInactivePosts(pageable, email);
                break;
            case "pending":
                page = postRepository.findPendingPosts(pageable, email);
                break;
            case "declined":
                page = postRepository.findDeclinedPosts(pageable, email);
                break;
            default:
                page = postRepository.findPublishedPosts(pageable, email);
        }
        posts.addAll(page.getContent());
        apiPostListResponse.setCount(page.getTotalElements());
        List<PostDto> postDtoList = posts.stream().map(mapperService::convertPostToDto)
                .collect(Collectors.toList());
        apiPostListResponse.setPosts(postDtoList);
        return apiPostListResponse;
    }

    public User getAuthorizedUser(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("user " + email + " not found"));
    }

    public PostForModerationListResponse getPostsForModeration(
            int offset, int limit, String status, Principal principal) {
        PostForModerationListResponse moderationListResponse = new PostForModerationListResponse();
        int moderatorId = getAuthorizedUser(principal).getId();
        List<Post> posts = new ArrayList<>();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> page;
        switch (status) {
            case "accepted":
                page = postRepository.findAcceptedPostsByModerator(pageable, moderatorId);
                break;
            case "declined":
                page = postRepository.findDeclinedPostsByModerator(pageable, moderatorId);
                break;
            default:
                page = postRepository.findNewPosts(pageable);
        }
        posts.addAll(page.getContent());
        moderationListResponse.setCount(page.getTotalElements());
        List<PostForModerationResponse> moderatorPosts = posts.stream()
                .map(mapperService::convertPostToDto)
                .map(p -> new PostForModerationResponse(p.getId(), p.getTimestamp(),
                        p.getTitle(), p.getAnnounce(), p.getLikeCount(),
                        p.getDislikeCount(), p.getCommentCount(), p.getViewCount(), p.getUser()))
                .collect(Collectors.toList());
        moderationListResponse.setPosts(moderatorPosts);
        return moderationListResponse;
    }

    public HashMap<String, String> getErrors(PostRequest postRequest) {
        HashMap<String, String> errors = new HashMap<>();
        if (postRequest.getTitle().length() < MIN_TITLE_LENGTH) {
            errors.put("title", "Заголовок не установлен");
        }
        if (postRequest.getText().length() < MIN_TEXT_LENGTH) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        return errors;
    }

    public RegResponse savePost(PostRequest postRequest, Principal principal) {
        RegResponse regResponse = new RegResponse();
        Map<String, String> errors = getErrors(postRequest);
        User user = getAuthorizedUser(principal);
        Date postDate = new Date(postRequest.getTimestamp() * 1000);
        if (errors.isEmpty()) {
            regResponse.setResult(true);
            Post post = new Post();
            post.setIsActive(postRequest.isActive() ? (byte) 1 : 0);
            post.setStatus(ModerationStatus.NEW);
            post.setTime(postDate.compareTo(new Date()) <= 0 ? new Date() : postDate);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            post.setViewCount(0);
            post.setUser(user);
            postRepository.save(post);
        } else {
            regResponse.setResult(false);
            regResponse.setErrors(errors);
        }
        return regResponse;
    }

    public RegResponse editPost(Post post, PostRequest postRequest, Principal principal) {
        RegResponse regResponse = new RegResponse();
        Map<String, String> errors = getErrors(postRequest);
        User user = getAuthorizedUser(principal);
        Date postDate = new Date(postRequest.getTimestamp() * 1000);
        if (errors.isEmpty()) {
            regResponse.setResult(true);
            post.setIsActive(postRequest.isActive() ? (byte) 1 : 0);
            if (user.getIsModerator() == 0) {
                post.setStatus(ModerationStatus.NEW);
            }
            post.setTime(postDate.compareTo(new Date()) <= 0 ? new Date() : postDate);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            postRepository.save(post);
        } else {
            regResponse.setResult(false);
            regResponse.setErrors(errors);
        }
        return regResponse;
    }
}
