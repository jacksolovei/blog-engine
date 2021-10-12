package main.service;

import main.api.response.TagListResponse;
import main.dto.TagDto;
import main.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MapperService mapperService;

    public TagListResponse getTags() {
        TagListResponse tagListResponse = new TagListResponse();
        List<Tag> tags = tagRepository.findAll();
        List<TagDto> tagDtoList = tags.stream().map(t -> mapperService.convertTagToDto(t))
                .collect(Collectors.toList());
        tagListResponse.setTags(tagDtoList);
        return tagListResponse;
    }

    public TagListResponse getTagByQuery(String query) {
        TagListResponse tagListResponse = new TagListResponse();
        Tag tag = tagRepository.findTagByName(query);
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(mapperService.convertTagToDto(tag));
        tagListResponse.setTags(tagDtoList);
        return tagListResponse;
    }
}
