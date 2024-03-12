package com.jwt.implementation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jwt.implementation.model.FileDB;
import com.jwt.implementation.model.ResponseFile;
import com.jwt.implementation.service.FileStorageService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    FileStorageService storageService;

    @CrossOrigin
    @PostMapping("/uploadsinglefile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
        System.out.println(username);
            storageService.store(file,username);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }


    // @CrossOrigin
    // @PostMapping("/uploadmultiplefile")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    // public ResponseEntity<String> uploadMultipleFiles(@RequestParam("file") List<MultipartFile> file) {
    //     String message = "";
    //     try {

    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// 	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	// 	String username = userDetails.getUsername();
    //         storageService.store(file,username);

    //         message = "Uploaded the file successfully: " + file.getOriginalFilename();
    //         return ResponseEntity.status(HttpStatus.OK).body(message);
    //     } catch (Exception e) {
    //         message = "Could not upload the file: " + file.getOriginalFilename() + "!";
    //         return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    //     }
    // }

}
