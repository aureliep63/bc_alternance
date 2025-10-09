package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.dto.MediaDto;
import com.example.BC_alternance.mapper.MediaMapper;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.service.MediaService;

import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/medias")
@Validated
public class MediaRestController {

    private MediaService mediaService;
    private MediaMapper mediaMapper;

    public MediaRestController(MediaService mediaService, MediaMapper mediaMapper) {
        this.mediaMapper = mediaMapper;
        this.mediaService = mediaService;
    }

    @GetMapping("")
    public List<MediaDto> getAllMedias(){
        return mediaService.getAllMedias();
    }

    @GetMapping("/{id}")
    public MediaDto getLieuxById(@PathVariable Long id) {
        return mediaService.getMediaById(id);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MediaDto saveMedia(@Valid @RequestBody MediaDto mediaDto){

        Media media = mediaService.saveMedia(mediaDto);
        return this.mediaMapper.toDto(media);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteMedia(@PathVariable Long id){
        mediaService.deleteMedia(id);
    }

    @PutMapping("/{id}")
    public MediaDto updateMedia(@PathVariable Long id, @Valid @RequestBody MediaDto mediaDto){
        mediaDto.setId(id);

        Media mediaUpdated = mediaService.saveMedia(mediaDto);
        return this.mediaMapper.toDto(mediaUpdated);

    }


}
