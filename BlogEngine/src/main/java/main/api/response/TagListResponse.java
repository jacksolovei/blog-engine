package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.dto.TagDto;

import java.util.List;

@Getter
@Setter
public class TagListResponse {
    private List<TagDto> tags;
}
