package main.api.response;


import java.util.List;

public class ApiPostListResponse {
    private int count;
    private List<ApiPostResponse> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ApiPostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<ApiPostResponse> posts) {
        this.posts = posts;
    }
}
