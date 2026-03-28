package com.ministorage.api.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ministorage.api.dto.FileDto;
import com.ministorage.api.entity.Folder;
import com.ministorage.api.service.FileService;
import com.ministorage.api.service.FolderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    
    
    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody Folder request) {
        Folder folder = folderService.createFolder(request.getName(), request.getParentId(), request.getUserId());
        return ResponseEntity.ok(folder);
    }
    
    @GetMapping("/contents")
    public ResponseEntity<?> getContents(
            @RequestParam("userId") Long userId, 
            @RequestParam(value = "folderId", required = false) Long folderId
            ) {
        return ResponseEntity.ok(folderService.getContents(userId, folderId));
    }
    
    
    
}