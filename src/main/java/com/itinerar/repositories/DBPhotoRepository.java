package com.itinerar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itinerar.entity.Photo;

@Repository
public interface DBPhotoRepository  extends JpaRepository<Photo, Long>{

}
