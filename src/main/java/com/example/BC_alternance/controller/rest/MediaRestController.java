package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.dto.MediaDto;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medias")
@Validated
public class MediaRestController {

    private MediaService mediaService;

    public MediaRestController(MediaService mediaService){
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
    public Media saveMedia(@Valid @RequestBody MediaDto mediaDto){
        return mediaService.saveMedia(mediaDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteMedia(@PathVariable Long id){
        mediaService.deleteMedia(id);
    }

    @PutMapping("/{id}")
    public Media updateMedia(@PathVariable Long id, @Valid @RequestBody MediaDto mediaDto){
        mediaDto.setId(id);
        return mediaService.saveMedia(mediaDto);

    }
}
