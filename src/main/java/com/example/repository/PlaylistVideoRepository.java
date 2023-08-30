package com.example.repository;
import com.example.entity.PlayListEntity;
import com.example.entity.PlaylistVideoEntity;
import com.example.mapper.PlaylistMapperI2;
import com.example.mapper.PlaylistVideoMapperI;
import com.example.mapper.VideoShortInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, String> {
    @Query("select new com.example.mapper.VideoShortInfoMapper(p.video.id,p.video.title,p.video.attach.duration) " +
            " from PlaylistVideoEntity as p where p.playlistId=:playlistId")
    List<VideoShortInfoMapper> findVideoByPlaylistId(@Param("playlistId") String playlistId);
    Optional<PlaylistVideoEntity>findByPlaylistIdAndVideoIdAndVisibleTrue(String playlistId, String videoId);

    @Query(value ="select p.id as playlistId, p.name as playlistName, count(v.id) as videoCount, " +
            "sum(v.view_count) as totalViewCount, (select created_date from playlist_video" +
            " where playlist_id=:playlistId and visible=true" +
            " order by created_date desc " +
            " limit 1 ) as lastUpdateDate from playlist_video as pv " +
            " inner join playlist as p on pv.playlist_id=p.id " +
            " inner join video as v on pv.video_id=v.id " +
            " where p.id=:playlistId and pv.visible=true" +
            " group by p.id, p.name" ,nativeQuery = true)
    PlaylistMapperI2 getInfoById(@Param("playlistId") String playlistId);
   @Transactional
   @Modifying
   @Query("update PlaylistVideoEntity set visible=false where playlistId=:playlistId and videoId=:videoId")
    int deleteVideo(@Param("playlistId") String playlistId, @Param("videoId") String videoId);

   //4. Get Video list by playListId (video status published)
    @Query("select pv.playlistId as playlistId, v.id as videoId, pa.id as previewAttachId, v.title as videoTitle," +
            " pa.duration as attachDuration, ch.id as channelId, ch.name as channelName," +
            " pv.createdDate as PVCreatedDate, pv.orderNumber as PVOrderNum from PlaylistVideoEntity as pv" +
            " inner join pv.playList as p inner join p.channelEntity as ch inner join pv.video as v inner join v.previewAttach pa" +
            "  where pv.playlistId=:playlistId and pv.visible=true and v.status=PUBLIC ")
   List<PlaylistVideoMapperI> getVideoListByPlaylistId(@Param("playlistId") String playlistId);


    @Query("select p from PlaylistVideoEntity as pv inner join pv.playList as p where pv.videoId=:videoId and pv.visible=true ")
    Optional<PlayListEntity> findAllByVideoIdAndVisibleTrue(@Param("videoId") String videoId);

}
