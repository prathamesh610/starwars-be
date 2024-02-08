package com.publicissapient.starwars.utility;

import com.publicissapient.starwars.service.InitializerService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MasterDataLoader {

    public static Map<Integer, String> swFilmsMap = new HashMap<>();
    public static Map<String, List<String>> swPeoplesMap = new HashMap<>();
    public static Map<String, List<String>> swPlanetsMap = new HashMap<>();
    public static Map<String, List<String>> swSpeciesMap = new HashMap<>();
    public static Map<String, List<String>> swStarshipMap = new HashMap<>();
    public static Map<String, List<String>> swVehicleMap = new HashMap<>();

    private final InitializerService initializerService;

    public MasterDataLoader(InitializerService initializerService){
        this.initializerService = initializerService;
    }


    @PostConstruct
    public void init(){
        initializerService.loadAllData();
    }
}
