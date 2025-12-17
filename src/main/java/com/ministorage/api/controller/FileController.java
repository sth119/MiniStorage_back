package com.ministorage.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ministorage.api.entity.File;
import com.ministorage.api.entity.User;
import com.ministorage.api.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;
	
	@GetMapping
	public ResponseEntity<List<File>> getFiles(@AuthenticationPrincipal  User user) {
		List<File> files = fileService.getFilesByUser(user);
		return ResponseEntity.ok(files);
	}
	
	@GetMapping("/trash")
	public ResponseEntity<List<File>> getTrashedFiles(@AuthenticationPrincipal User user) {
		List<File> files = fileService.getTrashedByUser(user);
        return ResponseEntity.ok(files);
	}
	
	// 3. 파일 생성
    @PostMapping
    public ResponseEntity<File> createFile(
            @AuthenticationPrincipal User user,
            @RequestBody FileCreateRequest request) {

        File file = fileService.createFile(
                user,
                request.title(),
                request.url(),
                request.type()
        );
        return ResponseEntity.ok(file);
    }

    // 4. 파일 순서 변경
    @PatchMapping("/order")
    public ResponseEntity<Void> updateOrder(
            @AuthenticationPrincipal User user,
            @RequestBody List<Long> fileIdsInNewOrder) {

        fileService.updateFileOrder(user, fileIdsInNewOrder);
        return ResponseEntity.ok().build();
    }

    // 5. 휴지통으로 이동
    @PatchMapping("/trash")
    public ResponseEntity<Void> moveToTrash(
            @AuthenticationPrincipal User user,
            @RequestBody Set<Long> fileIds) {

        fileService.moveToTrash(user, fileIds);
        return ResponseEntity.ok().build();
    }

    // 6. 복구
    @PatchMapping("/restore")
    public ResponseEntity<Void> restoreFromTrash(
            @AuthenticationPrincipal User user,
            @RequestBody Set<Long> fileIds) {

        fileService.restoreFromTrash(user, fileIds);
        return ResponseEntity.ok().build();
    }

    // 7. 영구 삭제
    @DeleteMapping
    public ResponseEntity<Void> permanentlyDelete(
            @AuthenticationPrincipal User user,
            @RequestBody Set<Long> fileIds) {

        fileService.permanentlyDelete(user, fileIds);
        return ResponseEntity.ok().build();
    }
    
    // 파일 생성용 DTO
    record FileCreateRequest(String title, String url, String type) {}
    
    
}
