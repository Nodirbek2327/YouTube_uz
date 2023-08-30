package com.example.service;

import com.example.dto.TagDTO;
import com.example.entity.TagEntity;
import com.example.exp.ItemNotFoundException;
import com.example.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(TagDTO tagDTO) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagDTO.getName());
        tagRepository.save(tagEntity);
        tagDTO.setId(tagEntity.getId());
        return tagDTO;
    }

    public TagDTO update(Integer id, TagDTO tagDTO) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Tag not found");
        TagEntity entity = optional.get();
        entity.setName(tagDTO.getName());
        tagRepository.save(entity);
        tagDTO.setId(entity.getId());
        return tagDTO;
    }

    public String delete(Integer id) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Tag not found");
        tagRepository.deleteById(id);
        return "Deleted!!!";
    }

    public List<TagDTO> getList() {
        List<TagDTO> tagList = new LinkedList<>();
        Iterable<TagEntity> all = tagRepository.findAll();
        all.forEach(tagEntity -> {
            tagList.add(toDTO(tagEntity));
        });
        return tagList;
    }


    public TagDTO toDTO(TagEntity tagEntity) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tagEntity.getId());
        tagDTO.setName(tagEntity.getName());
        tagDTO.setCreatedDate(tagEntity.getCreatedDate());
        return tagDTO;
    }


}
