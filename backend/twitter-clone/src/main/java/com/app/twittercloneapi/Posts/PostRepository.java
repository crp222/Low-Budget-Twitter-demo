package com.app.twittercloneapi.Posts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post,Integer>{
    

    @Query("SELECT p FROM Post p WHERE p.parent = -1 ORDER BY p.date DESC LIMIT ?1 OFFSET ?2")
    List<Post> postsDateOrderInRange(int limit,int offset);

    @Query("SELECT p FROM Post p WHERE p.parent = ?1 ORDER BY p.date DESC")
    List<Post> postCommentsDateOrderInRange(int id);
}
