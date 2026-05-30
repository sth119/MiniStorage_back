package com.ministorage.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ministorage.api.entity.File;
import com.ministorage.api.entity.User;
import com.ministorage.api.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Transactional
    public File uploadFile(User user, MultipartFile multipartFile) {
    	if (multipartFile.isEmpty()) {
    		throw new RuntimeException("파일이 비어있습니다.");
    		
    	}
		
    	try {
    		// 1. 저장할 디렉토리가 없으면 생성
    		Path uploadPath = Paths.get(uploadDir);
    		if (!Files.exists(uploadPath)) {
    			Files.createDirectories(uploadPath);
    		}
    		
    		// 2. 파일명 중복 방지
    		String originalFileName = multipartFile.getOriginalFilename();
    		String uuid = UUID.randomUUID().toString();
    		String extension = "";
    		
    		// 확장자 추출
    		int i = originalFileName.lastIndexOf('.');
    		if ( i > 0 ) {
    			extension = originalFileName.substring(i);
    		}
    		
    		String saveFileName = uuid + extension;
    		
    		// 3. 실제 파일 저장 ( 로컬 폴더로 복사 )
    		Path targetLocation = uploadPath.resolve(saveFileName);
    		Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    		
    		
    		// 4. DB에 정보 저장
    		File file = new File();
    		file.setUser(user);
    		file.setTitle(originalFileName);
    		file.setOriginalFilename(originalFileName);
    		
    		file.setUrl("/uploads/" + saveFileName);
    		
    		file.setType("file");
    		file.setFileSize(multipartFile.getSize());
    		file.setTrashed(false);
    		file.setCreatedAt(LocalDateTime.now());
    		
    		// 맨 마지막에 추가
    		List<File> existingFiles = getFilesByUser(user);
    		file.setOrderIndex(existingFiles.size());
    		
    		return fileRepository.save(file);
    		
    		
    	} catch(IOException e) {
    		throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
    	}
    }
    
    // 1. 유저별 일반 파일 조회 (휴지통 제외, 순서대로) 
    public List<File> getFilesByUser(User user) {
        return fileRepository.findByUserIdAndTrashedFalseOrderByOrderIndexAsc(user.getId());
    }
    
    // 2. 유저별 휴지통 파일 조회
    public List<File> getTrashedByUser(User user) {
        return fileRepository.findByUserIdAndTrashedTrue(user.getId());
    }
    
    // 3. 파일 생성
    @Transactional
    public File createFile(User user, String title, String url, String type, Long folderId) {
       
        File file = new File();
        
        file.setTitle(title.trim());
        file.setUrl(url.trim());
        file.setType(type != null ? type.trim() : "link");
        file.setTrashed(false); // 기본값 명시
        file.setFolderId(folderId);
        
        // 로그인 유저일 때만 DB 저장
        if (user != null) {
            file.setUser(user);
            
            // ★ 추가: 새 파일은 순서 맨 마지막에 넣기 위해 현재 개수 가져오기 (Null 방지)
            // (간단하게 구현하기 위해 일단 0이나 큰 값으로 넣어도 되지만, 이게 더 안전함)
            List<File> existingFiles = getFilesByUser(user);
            file.setOrderIndex(existingFiles.size()); 

            return fileRepository.save(file);
        } else {
            // 게스트용 (DB 저장 안 함)
            return file;
        }
    }

    // 4. 파일 순서 변경 (드래그 앤 드롭)
    @Transactional
    public void updateFileOrder(User user, List<Long> fileIdsInNewOrder) {
        for (int i = 0; i < fileIdsInNewOrder.size(); i++) {
            Long fileId = fileIdsInNewOrder.get(i);
            final int index = i; // 람다식용 final 변수
            
            fileRepository.findById(fileId).ifPresent(file -> {
                if (file.getUser().getId().equals(user.getId())) {
                    file.setOrderIndex(index);
                }
            });
        }
    }

    // 5. 파일 휴지통으로 이동 (soft delete)
    @Transactional
    public void moveToTrash(User user, Set<Long> fileIds) {
        fileIds.forEach(fileId -> {
            fileRepository.findById(fileId).ifPresent(file -> {
                if (file.getUser().getId().equals(user.getId())) {
                    file.setTrashed(true);
                }
            });
        });
    }

    // 6. 휴지통에서 복구
    @Transactional
    public void restoreFromTrash(User user, Set<Long> fileIds) {
        fileIds.forEach(fileId -> {
            fileRepository.findById(fileId).ifPresent(file -> {
                if (file.getUser().getId().equals(user.getId())) {
                    file.setTrashed(false);
                }
            });
        });
    }

    // 7. 영구 삭제
    @Transactional
    public void permanentlyDelete(User user, Set<Long> fileIds) {
        fileIds.forEach(fileId -> {
            fileRepository.findById(fileId).ifPresent(file -> {
                if (file.getUser().getId().equals(user.getId())) {
                    fileRepository.delete(file);
                }
            });
        });
    }
    
    // 8. 파일 이동 로직
    @Transactional
    public void moveFiles(List<Long> fileIds, Long targetFolderId, Long userId) {
        List<File> filesToMove = fileRepository.findAllById(fileIds);

        for (File file : filesToMove) {
            // 프론트에서 넘어온 userId와 파일 주인의 userId가 같은지 확인
        	if (!file.getUser().getId().equals(userId)) {
        	    throw new IllegalArgumentException("권한이 없는 파일입니다.");
        	}
            file.setFolderId(targetFolderId); 
        }

        fileRepository.saveAll(filesToMove);
    }
    
} // end Service