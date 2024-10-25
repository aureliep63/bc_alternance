package com.example.BC_alternance.service;

import com.example.BC_alternance.dto.MediaDto;
import com.example.BC_alternance.model.Media;

import java.util.List;

public interface MediaService {

    List<MediaDto> getAllMedias();

    MediaDto getMediaById(Long id);

    Media saveMedia(MediaDto mediaDto);

    void deleteMedia(Long id);
}
