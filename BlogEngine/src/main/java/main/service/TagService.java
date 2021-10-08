package main.service;

import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    TagListResponse tagListResponse = new TagListResponse();
    ArrayList<TagResponse> tags = new ArrayList<>();

    public TagListResponse getTags() {
        List<Tag> tagsFromRep = tagRepository.findAll();
        for (Tag tag : tagsFromRep) {
            this.addToList(tag.getName());
        }
        tagListResponse.setTags(tags);
        return tagListResponse;
    }

    public TagListResponse getTagByQuery(String query) {
        this.addToList(query);
        tagListResponse.setTags(tags);
        return tagListResponse;
    }

    private void addToList(String tag) {
        double countPostsByPopularTag = postRepository.findPostCountByPopularTag();
        double count = postRepository.findActivePostsCount();
        double countPostsByTag = postRepository.findPostCountByTag(tag);
        double weight = (double) Math.round((countPostsByTag / count) * (1 / (countPostsByPopularTag / count)) * 100) / 100;
        tags.add(new TagResponse(tag, weight));
    }
}
