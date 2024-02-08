package com.publicissapient.starwars.controller;

import com.publicissapient.starwars.constants.Constants;
import com.publicissapient.starwars.dto.ResponseDTO;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.service.OfflineService;
import com.publicissapient.starwars.service.OnlineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ApiControllerTest {

    @InjectMocks
    private ApiController apiController;

    @Mock
    private OfflineService offlineService;

    @Mock
    private OnlineService onlineService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFind() throws SWException {
        SWResponseDTO swResponseDTO = new SWResponseDTO();
        when(onlineService.findByName(any(), any())).thenReturn(swResponseDTO);
        when(offlineService.findByName(any(), any())).thenReturn(swResponseDTO);

        EntityModel<SWResponseDTO> result = apiController.find("People", "Luke Skywalker");

        assertEquals(swResponseDTO, result.getContent());
    }

    @Test
    public void testToggleStatus() {
        String response = "Value set: false";
        EntityModel<ResponseDTO> result = apiController.toggleStatus(false);

        assertNotNull(result.getContent());
        assertEquals(response, result.getContent().getData());
    }

    @Test
    public void testGetStatus() {
        String response = "value is: " + Constants.Status;
        EntityModel<ResponseDTO> result = apiController.getStatus();

        assertNotNull(result.getContent());
        assertEquals(response, result.getContent().getData());
    }
}