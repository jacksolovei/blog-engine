package main.api.response;

import java.util.ArrayList;

public class TagListResponse {
    private ArrayList<TagResponse> tags;

    public ArrayList<TagResponse> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagResponse> tags) {
        this.tags = tags;
    }
}
