package com.example.controller;

import com.example.dto.ApiResponeDTO;
import com.example.dto.ChannelDTO;
import com.example.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {
  @Autowired
    private ChannelService channelService;
  @PreAuthorize("hasRole('USER')")
  @PostMapping(value = "/create")
  public ResponseEntity<ApiResponeDTO> create(@Valid @RequestBody ChannelDTO dto){
    return ResponseEntity.ok(channelService.create(dto));
  }
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "/updateName")
  public ResponseEntity<ApiResponeDTO> updateName(@RequestParam("oldName") String oldName,
                                            @RequestParam("newName") String newName){
    return ResponseEntity.ok(channelService.updateName(oldName,newName));
  }
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "/updateImage")
  public ResponseEntity<ApiResponeDTO> updateImage(@RequestParam("name") String channelName,
                                             @RequestParam("imageId") String imageId){
    return ResponseEntity.ok(channelService.updatePhoto(imageId,channelName));
  }
  @PreAuthorize("hasRole('USER')")
  @PutMapping(value = "/updateBanner")
  public ResponseEntity<ApiResponeDTO> updateBanner(@RequestParam("name") String channelName,
                                             @RequestParam("bannerId") String bannerId){
    return ResponseEntity.ok(channelService.updateBanner(bannerId,channelName));
  }
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping(value = "/pagination")
  public ResponseEntity<PageImpl<ChannelDTO>> pagination(@RequestParam("page") int page,
                                                         @RequestParam("size") int size){
    return ResponseEntity.ok(channelService.pagination(page-1,size));
  }

  @GetMapping(value = "/getById/{id}")
  public ResponseEntity<ApiResponeDTO> getById(@PathVariable("id") String id){
    return ResponseEntity.ok(channelService.getChanelById(id));
  }
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  @PutMapping(value = "/changeStatus/{id}")
  public ResponseEntity<ApiResponeDTO> changeStatus(@PathVariable("id") String id){
    return ResponseEntity.ok(channelService.changeStatus(id));
  }
  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping(value = "/getChannels")
  public ResponseEntity<ApiResponeDTO> getChannels(){
    return ResponseEntity.ok(channelService.getChannelList());
  }

}
