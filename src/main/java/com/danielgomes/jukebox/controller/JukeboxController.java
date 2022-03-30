package com.danielgomes.jukebox.controller;

import com.danielgomes.jukebox.model.Jukebox;
import com.danielgomes.jukebox.model.Setting;
import com.danielgomes.jukebox.service.JukeboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name="Jukebox", description = "Jukebox Rest Controller")
public class JukeboxController {

    @Autowired
    private JukeboxService jukeboxService;

    @Operation(operationId = "findJukeboxes", summary = "Find Jukeboxes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Jukebox not found.", content = @Content)
    })
    @GetMapping(value = "/jukes", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findJukeboxes(@RequestParam(name = "settingId") String settingId,
                                           @RequestParam(name = "model", required = false) String model,
                                           @RequestParam(name = "offset", required = false) Integer offset,
                                           @RequestParam(name = "limit", required = false) Integer limit) {

        if (StringUtils.isEmpty(settingId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The settingId parameter is required.");
        }

        Optional<Setting> setting = getSettingById(settingId);

        if (setting.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());

        List<Jukebox> jukes = jukeboxService.getAllJukes().stream()
                .filter(j -> (StringUtils.isEmpty(model) || j.getModel().equals(model)))
                .filter(j -> j.componentsAsString().containsAll(setting.get().getRequires()))
                .collect(Collectors.toList());

        if (Objects.nonNull(offset) && Objects.nonNull(limit) && (offset > 0) && (limit > 0) && (offset < limit)) {
            int end = Math.min((offset + limit), jukes.size());
            return !jukes.isEmpty() ? ResponseEntity.ok().body(jukes.subList(offset, end)) : ResponseEntity.notFound().build();
        }

        return !jukes.isEmpty() ? ResponseEntity.ok().body(jukes) : ResponseEntity.notFound().build();
    }

    private Optional<Setting> getSettingById(String settingId) {

        return jukeboxService.getAllSettings().getSettings().stream().filter(s -> s.getId().equals(settingId)).findAny();

    }

}
