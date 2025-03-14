package com.example.BC_alternance.service.impl;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.dto.MediaDto;
import com.example.BC_alternance.mapper.LieuxMapper;
import com.example.BC_alternance.mapper.MediaMapper;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Media;
import com.example.BC_alternance.repository.BorneRepository;
import com.example.BC_alternance.repository.MediaRepository;
import com.example.BC_alternance.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    private MediaRepository mediaRepository;
    private BorneRepository borneRepository;
    private MediaMapper mediaMapper;

    public MediaServiceImpl(MediaRepository mediaRepository, BorneRepository borneRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.borneRepository = borneRepository;
        this.mediaMapper = mediaMapper;
    }

    private MediaDto mediaDto;

    @Override
    public List<MediaDto> getAllMedias(){
        List<Media> medias = mediaRepository.findAll();
        return medias.stream()
                        .map(mediaMapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public MediaDto getMediaById(Long id){
        return mediaRepository.findById(id)
                                .map(mediaMapper::toDto)
                                .orElse(null);
    }

    @Override
    public Media saveMedia(MediaDto mediaDto){
        Media media = mediaMapper.toEntity(mediaDto);

        if(mediaDto.getBorneId() != null){
            Borne borne = borneRepository.findById(mediaDto.getBorneId()).orElse(null);
            media.setBorne(borne);
        }
        return mediaRepository.save(media);
    }

    @Override
    public void deleteMedia(Long id){
        mediaRepository.deleteById(id);
    }
}
