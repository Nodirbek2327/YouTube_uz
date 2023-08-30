package com.example.service;

import com.example.dto.TagDTO;
import com.example.entity.VideoTagsEntity;
import com.example.repository.VideoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoTagService {
    @Autowired
    private VideoTagRepository videoTagRepository;

    public void create(String videoId, List<Integer> tagsList) {
        tagsList.forEach(tagId -> {
            save(videoId, tagId);
        });
    }

    public void save(String videoId, Integer tagId) {
        VideoTagsEntity entity = new VideoTagsEntity();
        entity.setVideoId(videoId);
        entity.setTagId(tagId);
        videoTagRepository.save(entity);
    }

    public void merge(String videoId, List<Integer> newList) {
        List<Integer> oldList = videoTagRepository.getOldVideoTagIdList(videoId);
        for (Integer tagId : newList) {
            if (!oldList.contains(tagId)) {
                save(videoId, tagId);
            }
        }
        for (Integer tagId : oldList) {
            if (!newList.contains(tagId)) {
                videoTagRepository.deleteByVideoIdAndAndTagId(videoId, tagId);
            }
        }
    }

    public List<TagDTO> get(String videoId) {
        List<VideoTagsEntity> list = videoTagRepository.findAllByVideoIdAndVisibleTrue(videoId);
        return list.stream().map(e -> {
            return new TagDTO(e.getTag().getId(), e.getTag().getName());
        }).toList();
    }
}
