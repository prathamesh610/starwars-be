package com.publicissapient.starwars.service;

import com.publicissapient.starwars.constants.SwApiConstants;
import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.factory.SwComponentModelFactory;
import com.publicissapient.starwars.model.SWBaseModel;
import com.publicissapient.starwars.model.SWApiResponse;
import com.publicissapient.starwars.model.SwGeneric;
import com.publicissapient.starwars.service.impl.OnlineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OnlineServiceTest {

    @InjectMocks
    private OnlineServiceImpl onlineService;

    @Mock
    private SwComponentModelFactory swComponentModelFactory;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName() throws SWException {
        SWBaseModel swBaseModel = new SwGeneric();
        SWApiResponse<SwGeneric> swApiResponse = new SWApiResponse<>();
        swApiResponse.setResults(new ArrayList<>());
        when(swComponentModelFactory.getSwApi(any())).thenReturn(SwApiConstants.SW_PEOPLE_URL);
        when(swComponentModelFactory.getSwModel(any())).thenReturn(swBaseModel);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(swApiResponse));

        assertThrows(SWException.class, () ->onlineService.findByName(SwTypeConstants.People, "Luke Skywalker"));

    }

    @Test
    public void testFindByNameWithInvalidName() {
        SWApiResponse<SwGeneric> swApiResponse = new SWApiResponse<>();
        swApiResponse.setResults(new ArrayList<>());
        when(swComponentModelFactory.getSwApi(any())).thenReturn(SwApiConstants.SW_PEOPLE_URL);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(swApiResponse));

        assertThrows(SWException.class, () -> onlineService.findByName(SwTypeConstants.People, "Invalid Name"));
    }
}