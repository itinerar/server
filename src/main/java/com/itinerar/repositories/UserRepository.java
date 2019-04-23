package com.itinerar.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itinerar.entity.Post;
import com.itinerar.entity.User;
import com.itinerar.teste.Employee;
//https://dzone.com/articles/add-custom-functionality-to-a-spring-data-reposito
// Native queries: https://www.baeldung.com/spring-data-jpa-query
public interface UserRepository extends JpaRepository<User, Long>{

    @Query(value = "Select * from User u WHERE u.id = :id", nativeQuery = true)
    User findUserById(@Param("id") Long id);


    @Query(value = "select * from user where user.id in (SELECT e.friend_id FROM `user_friendships` e WHERE e.user_id  = :reqId and e.friend_id not in (SELECT e.user_id FROM `user_friendships` e  WHERE e.friend_id =  :reqId))", nativeQuery = true)
    List<User> findSentRequests(@Param("reqId") Long reqId);

    @Query(value = "select * from user where user.id in (SELECT e.friend_id FROM `user_friendships` e WHERE e.user_id  = :reqId and e.friend_id in (SELECT e.user_id FROM `user_friendships` e  WHERE e.friend_id =  :reqId))", nativeQuery = true)
    List<User> findFriends(@Param("reqId") Long reqId);

    @Query(value="select * from user where user.id in (SELECT e.user_id FROM `user_friendships` e WHERE e.friend_id = :reqId and e.user_id  not in ( SELECT friend_id FROM `user_friendships` WHERE `user_id` = :reqId ))", nativeQuery = true)
    List<User> findReceivedRequests(@Param("reqId") Long reqId);


    @Query(value="select * from user where email = :email", nativeQuery = true) // la vrajeala
    User findUserByEmail(@Param("email") String email);

}
