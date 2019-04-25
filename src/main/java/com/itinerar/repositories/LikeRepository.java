package com.itinerar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itinerar.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
	
	@Query(value="select count(*) FROM likes where post_id = :postId and user_id = :userId", nativeQuery = true)
	Long getNumberOfLikesOfTheUserForThePost(Long userId, Long postId);
	@Query(value="select count(*) FROM likes where photo_id = :photoId and user_id = :userId", nativeQuery = true)
	Long getNumberOfLikesOfTheUserForThePhoto(Long userId, Long photoId);
	
	@Query(value="select count(*) FROM likes where post_id = :postId", nativeQuery = true)
	Long getNumberOfPostLikes(Long postId);
	@Query(value="select count(*) FROM likes where photo_id = :photoId", nativeQuery = true)
	Long getNumberOfPhotoLikes(Long photoId);
	
	@Query(value="select * FROM likes where post_id = :postId", nativeQuery = true)
	List<Like> getAllPostLikes(Long postId);
	
	@Query(value="select * FROM likes where photo_id = :photoId", nativeQuery = true)
	List<Like> getAllPhotoLikes(Long photoId);

}
