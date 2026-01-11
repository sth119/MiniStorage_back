package com.ministorage.api.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ministorage.api.entity.File;
import com.ministorage.api.entity.User;
import com.ministorage.api.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    
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
    public File createFile(User user, String title, String url, String type) {
       
        File file = new File();
        
        file.setTitle(title.trim());
        file.setUrl(url.trim());
        file.setType(type != null ? type.trim() : "link");
        file.setTrashed(false); // 기본값 명시
        
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
    
} // end Service