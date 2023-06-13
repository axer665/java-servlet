package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Stub
public class PostRepository {
  private int postId = 1;
  private List<Post> posts = new CopyOnWriteArrayList();

  public List<Post> all() {
    return this.posts;
  }

  public Post getById(long id) {
    for (int i = 0; i < this.posts.size(); i++) {
      Post searchPost = this.posts.get(i);
      if (searchPost.getId() == id) {
        return posts.get(i);
      }
    }
    throw new NotFoundException("Post not found");
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(postId);
      postId++;
    } else {
      for (Post searchPost: this.posts) {
        if (post.getId() == searchPost.getId()){
          searchPost.setContent(post.getContent());
          return searchPost;
        }
      }
      // если не нашли - кидаем исключение
      throw new NotFoundException("Post not found");
    }
    this.posts.add(post);
    return post;
  }

  public void removeById(long id) {
    for (int i = 0; i < this.posts.size(); i++) {
      Post searchPost = this.posts.get(i);
      if (searchPost.getId() == id) {
        this.posts.remove(i);
      }
    }
  }
}
