package com.danielgomes.jukebox.controller;

import com.danielgomes.jukebox.model.Component;
import com.danielgomes.jukebox.model.Jukebox;
import com.danielgomes.jukebox.model.Setting;
import com.danielgomes.jukebox.model.Settings;
import com.danielgomes.jukebox.service.JukeboxService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

@WebMvcTest
class JukeboxControllerTest {

    @Autowired
    private JukeboxController jukeboxController;

    @MockBean
    private JukeboxService jukeboxService;

    private List<Jukebox> jukeboxes;
    private Settings settings;

    @BeforeEach
    void setUp() {
        standaloneSetup(this.jukeboxController);

        // Components
        Set<Component> components = new HashSet<>();
        components.add(Component.builder().name("led_matrix").build());
        components.add(Component.builder().name("money_receiver").build());
        components.add(Component.builder().name("speaker").build());
        components.add(Component.builder().name("money_pcb").build());
        components.add(Component.builder().name("touchscreen").build());

        Jukebox jukebox = Jukebox.builder()
                            .id("5ca94a8acc046e7aa8040605")
                            .model("angelina")
                            .components(components)
                            .build();

        this.jukeboxes = new ArrayList<>();
        this.jukeboxes.add(jukebox);

        // Requires
        List<String> requires = new ArrayList<>();
        requires.add("touchscreen");
        requires.add("money_pcb");

        // Setting
        Setting setting = Setting.builder()
                            .id("2321763c-8e06-4a31-873d-0b5dac2436da")
                            .requires(requires)
                            .build();

        List<Setting> listOfSettings = new ArrayList<>();
        listOfSettings.add(setting);

        this.settings = Settings.builder()
                            .settings(listOfSettings)
                            .build();
    }

    @Test
    void shouldReturnSuccessTest() {
        when(this.jukeboxService.getAllSettings()).thenReturn(this.settings);
        when(this.jukeboxService.getAllJukes()).thenReturn(this.jukeboxes);

        given()
            .accept(ContentType.JSON)
        .when()
            .get("/jukes?settingId={settingId}", "2321763c-8e06-4a31-873d-0b5dac2436da")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnNotFoundTest() {
        when(this.jukeboxService.getAllSettings()).thenReturn(this.settings);
        when(this.jukeboxService.getAllJukes()).thenReturn(this.jukeboxes);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/jukes?settingId={settingId}", "1234567890")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnBadRequestTest() {
        when(this.jukeboxService.getAllSettings()).thenReturn(this.settings);
        when(this.jukeboxService.getAllJukes()).thenReturn(this.jukeboxes);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/jukes?settingId={settingId}", "")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}