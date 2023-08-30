package com.example.repository;

import com.example.entity.VideoEntity;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoEntity, String>,
        PagingAndSortingRepository<VideoEntity, String> {

    @Query("from VideoEntity as v where v.id=:videoId and v.channel.profileId=:profileId and v.visible=true ")
    Optional<VideoEntity> getByIdAndProfileId(@Param("videoId") String videoId,
                                              @Param("profileId") Integer profileId);

    @Transactional
    @Modifying
    @Query("update VideoEntity set status=:status where id=:videoId and visible=true ")
    void updateStatus(@Param("videoId") String videoId,
                      @Param("status") AccessLevel status);

    @Transactional
    @Modifying
    @Query("update VideoEntity set viewCount=viewCount+1 where id=:videoId and visible=true ")
    int increaseViewCount(@Param("videoId") String videoId);

    Page<VideoEntity> findAllByCategoryIdAndVisibleTrue(Integer categoryId, Pageable pageable);

    @Query("from VideoEntity where title like %:title% and  visible=true ")
    List<VideoEntity> searchByTitle(String title);



    @Query("select count (VideoEntity ) from VideoEntity as v inner join v.videoTags as t " +
            "where t.tagId=:tagId and v.visible=true ")
    int getVideoCount(@Param("tagId") Integer tagId);

    Optional<VideoEntity> findAllByIdAndVisibleTrue(String videoId);

    Page<VideoEntity> findAllByChannelIdAndVisibleTrue(String channelId,Pageable pageable);
}
