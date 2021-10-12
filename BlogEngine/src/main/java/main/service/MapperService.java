package main.service;

import main.dto.PostDto;
import main.dto.TagDto;
import main.dto.UserDto;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MapperService {

    @Autowired
    private PostRepository postRepository;

    public PostDto convertPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setActive(post.getIsActive() == 1);
        postDto.setTimestamp(post.getTime().getTime() / 1000);
        postDto.setUser(convertUserToDto(post.getUser()));
        postDto.setTitle(post.getTitle());
        postDto.setAnnounce(post.getText());
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
        post.setText(postDto.getAnnounce());
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

    public TagDto convertTagToDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        tagDto.setWeight(getTagWeight(tag.getName()));
        return tagDto;
    }

    public Tag convertDtoToTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }

    private double getTagWeight(String tagName) {
        double countPostsByPopularTag = postRepository.findPostCountByPopularTag();
        double count = postRepository.findActivePostsCount();
        double countPostsByTag = postRepository.findPostCountByTag(tagName);
        return (double) Math.round((countPostsByTag / count) * (1 / (countPostsByPopularTag / count)) * 100) / 100;
    }
}
