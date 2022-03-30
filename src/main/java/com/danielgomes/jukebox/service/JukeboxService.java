package com.danielgomes.jukebox.service;

import com.danielgomes.jukebox.model.Jukebox;
import com.danielgomes.jukebox.model.Settings;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url= "http://my-json-server.typicode.com/touchtunes/tech-assignment/", name = "jukebox")
public interface JukeboxService {

    @GetMapping("settings")
    @Cacheable("settings")
    Settings getAllSettings(); //http://my-json-server.typicode.com/touchtunes/tech-assignment/settings

    @GetMapping("jukes")
    @Cacheable("jukes")
    List<Jukebox> getAllJukes(); //http://my-json-server.typicode.com/touchtunes/tech-assignment/jukes

}
