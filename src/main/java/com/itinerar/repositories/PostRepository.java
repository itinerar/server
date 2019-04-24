package com.itinerar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itinerar.entity.Comment;
import com.itinerar.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	@Query(value = "select * from Post",nativeQuery = true)
	List<Post> giveAllPosts();
	
	@Query(value="SELECT * FROM post ps where ps.journey_id IN ( SELECT js.id from journey js WHERE js.user_id = :reqId)", nativeQuery = true)
	List<Post> findAllUserPosts(@Param("reqId") Long reqId);
	
	
	@Query(value="SELECT * FROM post ps where (status = 'PUBLIC' or status = 'PROTECTED') and ps.journey_id IN ( SELECT js.id from journey js WHERE js.user_id in ( " + 
			"select id from user where user.id in (SELECT e.friend_id FROM `user_friendships` e WHERE e.user_id  = 2 and e.friend_id in (SELECT e.user_id FROM `user_friendships` e  WHERE e.friend_id =  :reqId))))", nativeQuery = true)
	List<Post> findAllUserFriendsPosts(@Param("reqId") Long reqId);

	@Query(value = "select * from Post where status = 'PUBLIC' ",nativeQuery = true)
	List<Post> retrieveAllPublicPosts();
	
	@Query(value = "select *  from post p where p.id = (select max(id) from post)",nativeQuery = true)
	Post getMaxPost();
	
	@Query(value = "Select * from post p WHERE p.journey_id = :journeyId", nativeQuery = true)
	List<Post> findAllJourneyPost(@Param("journeyId") Long journeyId);

	
	 

}
