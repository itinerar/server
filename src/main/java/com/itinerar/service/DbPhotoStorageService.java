package com.itinerar.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itinerar.entity.Like;
import com.itinerar.entity.Location;
import com.itinerar.entity.Photo;
import com.itinerar.entity.Post;
import com.itinerar.exception.FileStorageException;
import com.itinerar.exception.MyFileNotFoundException;
import com.itinerar.repositories.DBPhotoRepository;
import com.itinerar.teste.DBFileRepository;

public class DbPhotoStorageService {
	 @Autowired
	    private static DBPhotoRepository dbPhotoRepository;

	    public Photo createPhotoFromFile(MultipartFile file) {
	        // Normalize file name
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	        try {
	            // Check if the file's name contains invalid characters
	            if(fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }

	            Photo photo = new Photo(null, null,fileName, file.getContentType(), file.getBytes());

	            return photo; //dbPhotoRepository.save(photo);
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }

	    public static Photo getFile(Long fileId) {
	        return dbPhotoRepository.findById(fileId)
	                .orElseThrow(() -> new MyFileNotFoundException("Photo not found with id " + fileId));
	    }
}
