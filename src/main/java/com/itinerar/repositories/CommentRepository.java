package com.itinerar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itinerar.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query(value="select * FROM comment where post_id = :postId", nativeQuery = true)
	List<Comment> getAllPostComments(Long postId);
	@Query(value="select count(*) FROM comment where post_id = :postId", nativeQuery = true)
	Long getNumberOfComments(Long postId);
}
