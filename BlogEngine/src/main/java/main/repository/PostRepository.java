package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM posts WHERE is_active = 1 ORDER BY time DESC", countQuery = "SELECT COUNT(*) FROM posts", nativeQuery = true)
    Page<Post> findRecentPosts(Pageable pageable);
    //moderation status accepted

    @Query(value = "SELECT * FROM posts WHERE is_active = 1 ORDER BY time ASC", countQuery = "SELECT COUNT(*) FROM posts", nativeQuery = true)
    Page<Post> findEarlyPosts(Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE posts.is_active = 1 ORDER BY (SELECT COUNT(*) FROM post_comments WHERE post_id = posts.id) DESC", countQuery = "SELECT COUNT(*) FROM posts", nativeQuery = true)
    Page<Post> findPopularPosts(Pageable pageable);

   @Query(value = "SELECT * FROM posts WHERE posts.is_active = 1 ORDER BY (SELECT COUNT(*) FROM post_votes WHERE post_id = posts.id AND value = 1) DESC", countQuery = "SELECT COUNT(*) FROM posts", nativeQuery = true)
    Page<Post> findBestPosts(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1", nativeQuery = true)
    int findActivePostsCount();

    @Query(value = "SELECT COUNT(post_comments.id) FROM posts JOIN post_comments ON posts.id = post_comments.post_id WHERE posts.id = :postId AND posts.is_active = 1", nativeQuery = true)
    int findPostCommentsCount(@Param("postId") int postId);

    @Query(value = "SELECT COUNT(post_votes.id) FROM posts JOIN post_votes ON posts.id = post_votes.post_id WHERE posts.id = :postId AND posts.is_active = 1 AND post_votes.value = 1", nativeQuery = true)
    int findPostLikesCount(@Param("postId") int postId);

    @Query(value = "SELECT COUNT(post_votes.id) FROM posts JOIN post_votes ON posts.id = post_votes.post_id WHERE posts.id = :postId AND posts.is_active = 1 AND post_votes.value = -1", nativeQuery = true)
    int findPostDislikesCount(@Param("postId") int postId);

    @Query(value = "SELECT COUNT(*) AS 'count' FROM posts JOIN tag2post ON posts.id = tag2post.post_id JOIN tags ON tags.id = tag2post.tag_id WHERE tags.name = :tagName AND posts.is_active = 1", nativeQuery = true)
    int findPostCountByTag(@Param("tagName") String tagName);

    @Query(value = "SELECT COUNT(*) AS 'count' FROM posts JOIN tag2post ON posts.id = tag2post.post_id JOIN tags ON tags.id = tag2post.tag_id WHERE posts.is_active = 1 GROUP BY tags.name ORDER BY count DESC LIMIT 1", nativeQuery = true)
    int findPostCountByPopularTag();

    @Query(value = "SELECT COUNT(*) FROM posts WHERE is_active = 1 AND moderation_status = 'NEW'", nativeQuery = true)
    int findUnmoderatedPostsCount();
}
