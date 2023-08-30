package com.example.repository;

import com.example.entity.VideoTagsEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VideoTagRepository extends CrudRepository<VideoTagsEntity, String> {
    List<VideoTagsEntity> findAllByVideoIdAndVisibleTrue(String videoId);

    @Query("select a.tagId from VideoTagsEntity as a where a.videoId=:videoId and a.visible=true ")
    List<Integer> getOldVideoTagIdList(@Param("videoId") String videoId);

    @Transactional
    @Modifying
    @Query("update  VideoTagsEntity set visible=true where videoId=:videoId and tagId=:tagId")
    void deleteByVideoIdAndAndTagId(@Param("videoId") String videoId,
                                    @Param("tagId") Integer tagId);
}
