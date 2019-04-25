package com.itinerar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itinerar.entity.Location;
import com.itinerar.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long>{

}
