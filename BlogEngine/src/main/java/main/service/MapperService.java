package main.service;

import lombok.AllArgsConstructor;
import main.dto.PostDto;
import main.dto.UserDto;
import main.model.Post;
import main.model.User;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class MapperService {
    private final PostRepository postRepository;

    public PostDto convertPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setActive(post.getIsActive() == 1);
        postDto.setTimestamp(post.getTime().getTime() / 1000);
        postDto.setUser(convertUserToDto(post.getUser()));
        postDto.setTitle(post.getTitle());
        postDto.setText(post.getText());
        String postDtoText = postDto.getText();
        postDto.setAnnounce(postDtoText.length() < 150 ? postDtoText : postDtoText.substring(0, 150) + "...");
        postDto.setLikeCount(postRepository.findPostLikesCount(post.getId()));
        postDto.setDislikeCount(postRepository.findPostDislikesCount(post.getId()));
        postDto.setCommentCount(postRepository.findPostCommentsCount(post.getId()));
        postDto.setViewCount(post.getViewCount());
        return postDto;
    }

    public Post convertDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setIsActive(postDto.isActive() ? (byte) 1 : 0);
        post.setTime(new Date(postDto.getTimestamp() * 1000));
        post.setUser(convertDtoToUser(postDto.getUser()));
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        return post;
    }

    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setRegTime(user.getRegTime());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoto(user.getPhoto());
        userDto.setModeration(user.getIsModerator() == 1);
        if (userDto.isModeration()) {
            userDto.setModerationCount(postRepository.findUnmoderatedPostsCount());
            userDto.setSettings(true);
        } else {
            userDto.setModerationCount(0);
            userDto.setSettings(false);
        }
        return userDto;
    }

    public User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setRegTime(userDto.getRegTime());
        user.setPassword(userDto.getPassword());
        user.setIsModerator(userDto.isModeration() ? (byte) 1 : 0);
        user.setEmail(userDto.getEmail());
        user.setPhoto(userDto.getPhoto());
        return user;
    }
}
