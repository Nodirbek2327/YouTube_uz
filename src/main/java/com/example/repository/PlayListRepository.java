package com.example.repository;

import com.example.dto.PlayListDTO;
import com.example.entity.PlayListEntity;
import com.example.mapper.PlaylistMapperI;
import com.example.mapper.PlaylistShortInfoMapperI;
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

public interface PlayListRepository extends CrudRepository<PlayListEntity, String>,
        PagingAndSortingRepository<PlayListEntity,String> {
    Optional<PlayListEntity>findByOrderNumberAndChannelIdAndVisibleTrue(Integer orderNum, String channelId);

    @Query("from PlayListEntity as p inner join p.channelEntity as ch where" +
            " p.id=:id and ch.profileId=:profileId and p.visible=true ")
    Optional<PlayListEntity> findPlaylist(@Param("id") String playlistId,@Param("profileId")Integer profileId);
    @Transactional
    @Modifying
    @Query("update PlayListEntity set visible=false where id=:id")
    int deletePlaylist(@Param("id") String id);

    @Query("select p.id as playlistId, p.name as playlistName, p.description as playlistDescription," +
            " p.status as playlistStatus, p.orderNumber as playlistOrderNum, p.channelEntity.id as channelId, " +
            " p.channelEntity.name as channelName, p.channelEntity.image.id as channelPhotoId, " +
            " p.channelEntity.profile.id as profileId, p.channelEntity.profile.name as profileName," +
            " p.channelEntity.profile.surname as profileSurname, p.channelEntity.profile.image.id as profilePhotoId" +
            " from PlayListEntity as p where p.visible=true")
    Page<PlaylistMapperI>pagination(Pageable pageable);
    @Query("select p.id as playlistId, p.name as playlistName, p.description as playlistDescription," +
            " p.status as playlistStatus, p.orderNumber as playlistOrderNum, p.channelEntity.id as channelId, " +
            " p.channelEntity.name as channelName, p.channelEntity.image.id as channelPhotoId, " +
            " p.channelEntity.profile.id as profileId, p.channelEntity.profile.name as profileName," +
            " p.channelEntity.profile.surname as profileSurname, p.channelEntity.profile.image.id as profilePhotoId" +
            " from PlayListEntity as p where p.channelEntity.profile.id=:userId and p.visible=true " +
            " order by p.orderNumber desc ")
    List<PlaylistMapperI>getByUserId(@Param("userId") Integer userId);
    @Query("select p.id as playlistId, p.name as playlistName, p.createdDate as playlistCreatedDate," +
            " ch.id as channelId, p.channelEntity.name as channelName from PlayListEntity as p " +
            "inner join  p.channelEntity as ch where ch.profileId=:userId " +
            " order by p.orderNumber desc ")
    List<PlaylistShortInfoMapperI>getOwnPlaylist(@Param("userId") Integer userId);
    @Query("select p.id as playlistId, p.name as playlistName, p.createdDate as playlistCreatedDate," +
            " p.channelEntity.id as channelId, p.channelEntity.name as channeName from PlayListEntity as p " +
            " where p.channelId=:channelId and p.visible=true and p.status=PUBLIC " +
            " order by p.orderNumber desc ")
    List<PlaylistShortInfoMapperI> getByChannelId(@Param("channelId") String channelId);
}
