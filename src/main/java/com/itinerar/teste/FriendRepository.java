package com.itinerar.teste;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itinerar.entity.User;

public interface FriendRepository { // extends JpaRepository<Friendship, Long> {
	/*
	@Query(value = "Select * from Friendship u WHERE requester_username = :username", nativeQuery = true)
	List<Friendship> findFriends(@Param("username") String username);
	
	
	@Query(value = "Select fs.friend_id from friendship fs where fs.requester_id = :reqId and fs.active = 1", nativeQuery = true)
	Set<Friendship> findAllFriends(@Param("reqId") Long reqId);
	
	
	@Query(value = "INSERT INTO friendship values(34, true, :requestId, :newFriendId)", nativeQuery = true)
	void addRelation(@Param("requestId") Long requestId, @Param("newFriendId") Long newFriendId);
	*/
}
