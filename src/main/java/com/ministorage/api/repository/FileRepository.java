package com.ministorage.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ministorage.api.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

	List<File> findByUserIdAndTrashedFalseOrderByOrderIndexAsc(Long userId);
	
	List<File> findByUserIdAndTrashedTrue(Long userId);
}
