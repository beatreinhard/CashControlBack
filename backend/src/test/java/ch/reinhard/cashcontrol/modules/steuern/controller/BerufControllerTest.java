package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.BerufDto;
import ch.reinhard.cashcontrol.modules.steuern.api.BerufService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BerufController.class)
public class BerufControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BerufService service;

    @Test
    void getBerufByIdShouldReturn200AndExpectedDto() throws Exception {
        // GIVEN
        var expectedBerufDto = new BerufDto(
                "1",
                2023,
                "Reinhard",
                "BIT",
                "Zollikofen",
                80,
                200,
                100,
                "Zeitersparnis",
                new BigDecimal("14"),
                "");

        given(service.getBerufById(anyString())).willReturn(expectedBerufDto);

        // WHEN
        var result = this.mockMvc
                .perform(get("/api/v1/beruf/33").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Extrahiere  das JSON aus der Antwort
        var jsonResponse = result.getResponse().getContentAsString();

        // Konvertieren  des JSON in ein BerufDto-Objekt
        var objectMapper = new ObjectMapper();
        var actualBerufDto = objectMapper.readValue(jsonResponse, BerufDto.class);

        // THEN
        assertEquals(expectedBerufDto, actualBerufDto);
    }

    @Test
    void getBerufByIdShouldReturn404WhenNotFound() throws Exception {
        // GIVEN
        given(service.getBerufById(anyString())).willThrow(EntityNotFoundException.class);

        // WHEN... THEN...
        this.mockMvc
                .perform(get("/api/v1/beruf/12").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBerufShouldReturn201AndId() throws Exception {
        // GIVEN
        var requestBerufDto = new BerufDto(
                "1",
                2023,
                "Reinhard",
                "BIT",
                "Zollikofen",
                80,
                200,
                100,
                "Zeitersparnis",
                new BigDecimal("14"),
                "");

        given(service.createBeruf(any(BerufDto.class))).willReturn("generated-id");

        // WHEN
        var result = this.mockMvc
                .perform(post("/api/v1/beruf")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(requestBerufDto))) // Konvertieren des BerufDto in JSON
                .andExpect(status().isCreated())
                .andReturn();
        var respone = result.getResponse().getContentAsString();

        // THEBN
        assertEquals("generated-id", respone);
    }

    // Hilfsmethode zum Konvertieren von Objekten in JSON-Strings
    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
