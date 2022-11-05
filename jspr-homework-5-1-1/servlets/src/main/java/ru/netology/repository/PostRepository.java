package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final ConcurrentMap<Long, Post> repository;
  private final AtomicLong idCounter;

  public PostRepository() {
    this.repository = new ConcurrentHashMap<>();
    this.idCounter = new AtomicLong();
  }

  public List<Post> all() {
    return new ArrayList<>(repository.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.of(repository.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(idCounter.getAndIncrement());
      repository.put(post.getId(), post);
    } else {
      Post updatePost = Optional.ofNullable(repository.get(post.getId())).orElseThrow(NotFoundException::new);
      updatePost.setContent(post.getContent());
    }
    return post;
  }

  public void removeById(long id) {
    repository.remove(id);
  }
}