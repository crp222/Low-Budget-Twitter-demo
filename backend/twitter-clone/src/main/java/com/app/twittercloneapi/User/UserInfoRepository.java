package com.app.twittercloneapi.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,Integer>{
    
    @Query("SELECT user FROM UserInfo user WHERE user.username = ?1")
    Optional<UserInfo> findByUsername(String username);

    @Query("SELECT user.id FROM UserInfo user WHERE user.username = ?1")
    Optional<Integer> findUsernameId(String username);
}
