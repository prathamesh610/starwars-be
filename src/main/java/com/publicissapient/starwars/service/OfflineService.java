package com.publicissapient.starwars.service;

import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.SWException;

public interface OfflineService {
    SWResponseDTO findByName(SwTypeConstants swTypeConstants, String name) throws SWException;
}
