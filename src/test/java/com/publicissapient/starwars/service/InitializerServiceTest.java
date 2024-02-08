package com.publicissapient.starwars.service;
import com.publicissapient.starwars.model.SWApiResponse;
import com.publicissapient.starwars.model.SwGeneric;
import com.publicissapient.starwars.service.impl.InitializerServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class InitializerServiceTest {

    @InjectMocks
    private InitializerServiceImpl initializerService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadAllData() {
        SWApiResponse<SwGeneric> swApiResponse = new SWApiResponse<>();
        swApiResponse.setResults(new ArrayList<>());
        // Define the behavior of restTemplate.exchange() when called with any arguments
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(swApiResponse));

        // Call the method we're testing
        initializerService.loadAllData();

        // Verify the results (in this case, we're just checking that no exception was thrown)
        assertTrue(true);
    }

    // Add more test methods for other methods in InitializerServiceImpl
}