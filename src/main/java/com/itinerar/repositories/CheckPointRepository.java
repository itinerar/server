package com.itinerar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itinerar.entity.CheckPoint;
import com.itinerar.entity.Comment;

public interface CheckPointRepository extends JpaRepository<CheckPoint, Long>{

}
