package com.ministorage.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ministorage.api.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

	// 특정 유저의 최상위 폴더 목록 조회 ( parentId 가 null 인 경우 )
	List<Folder> findByUserIdAndParentIdIsNullAndIsTrashedFalse(Long userId);
	
	//  특정 폴더 내부의 하위 폴더 목록 조회.
	List<Folder> findByUserIdAndParentIdAndIsTrashedFalse(Long  userId,  Long  parentId);

	// 휴지통에 있는 폴더 목록 조회
	List<Folder> findByUserIdAndIsTrashedTrue(Long userId);
}
