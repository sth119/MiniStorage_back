package com.ministorage.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ministorage.api.entity.File;
import com.ministorage.api.entity.Folder;
import com.ministorage.api.repository.FileRepository;
import com.ministorage.api.repository.FolderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {

	private final FolderRepository folderRepository;
	private final FileRepository fileRepository;
	
	
	// create folder
	public Folder createFolder(String name, Long parentId, Long userId) {
		
		Folder folder = new Folder();
		
		folder.setName(name);
		folder.setParentId(parentId);
		folder.setUserId(userId);
		
		return folderRepository.save(folder);
	}
	
	public Map<String, Object> getContents(Long userId, Long folderId) {
		
		List<Folder> folders;
		List<File> files;
		
		if(folderId == null) {
			
			folders = folderRepository.findByUserIdAndParentIdIsNullAndIsTrashedFalse(userId);
			files = fileRepository.findByUserIdAndFolderIdIsNullAndTrashedFalse(userId);
		} else {
			folders = folderRepository.findByUserIdAndParentIdAndIsTrashedFalse(userId, folderId);
			files = fileRepository.findByUserIdAndFolderIdAndTrashedFalse(userId, folderId);
			
		}
		
		Map<String, Object> contents = new HashMap<>();
		contents.put("folders", folders);
		contents.put("files", files);
		return contents;
		
		
		
		}

} // end service
