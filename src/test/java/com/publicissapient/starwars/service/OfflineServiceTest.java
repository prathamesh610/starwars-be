package com.publicissapient.starwars.service;
import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.factory.SwComponentModelFactory;
import com.publicissapient.starwars.service.impl.OfflineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class OfflineServiceTest {

    @InjectMocks
    private OfflineServiceImpl offlineService;

    @Mock
    private SwComponentModelFactory swComponentModelFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName() throws SWException {
        when(swComponentModelFactory.getSWMap(any())).thenReturn(Map.of("Luke Skywalker", Collections.singletonList("Star Wars")));

        SWResponseDTO result = offlineService.findByName(SwTypeConstants.People, "Luke Skywalker");

        assertEquals("People", result.getType());
        assertEquals("Luke Skywalker", result.getName());
        assertEquals(Collections.singletonList("Star Wars"), result.getFilms());
        assertEquals(1, result.getCount());
    }

    @Test
    public void testFindByNameWithInvalidName() {
        when(swComponentModelFactory.getSWMap(any())).thenReturn(Map.of());

        assertThrows(SWException.class, () -> offlineService.findByName(SwTypeConstants.People, "Invalid Name"));
    }
}