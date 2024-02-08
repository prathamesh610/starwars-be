package com.publicissapient.starwars.controller;

import com.publicissapient.starwars.constants.Constants;
import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.ResponseDTO;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.ApplicationExceptionHandler;
import com.publicissapient.starwars.exception.ErrorCodes;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.service.OfflineService;
import com.publicissapient.starwars.service.OnlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v1")
@Tag(name = "StarWars API", description = "To get StarWars films related data from swapi.dev. You can give type and name to get the data, where type is in [People, Films, Starships, Vehicles, Species, Planets] and name is the name of the entity.")
public class ApiController {

    private final OfflineService offlineService;
    private final OnlineService onlineService;

    public ApiController(OfflineService offlineService, OnlineService onlineService){
        this.offlineService = offlineService;
        this.onlineService = onlineService;
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/find")
    @Operation(summary = "Find StarWars data by type and name", description = "Here you can give type and name to get the data, where type is in [People, Films, Starships, Vehicles, Species, Planets] and name is the name of the entity.")
    public EntityModel<SWResponseDTO> find(@RequestParam("type") String stringType, @RequestParam(value = "name") String name){
        SWResponseDTO swResponseDTO;
        try {
            SwTypeConstants type = this.getEnumFromType(stringType);
            if (Constants.Status) {
                swResponseDTO = onlineService.findByName(type, name);
            } else {
                swResponseDTO = offlineService.findByName(type, name);
            }
        }catch (SWException e){
            throw new ApplicationExceptionHandler.ApplicationWrappedException(e);
        }catch (Exception e){
            throw new ApplicationExceptionHandler.WrappedException(e);
        }
        EntityModel<SWResponseDTO> resource = EntityModel.of(swResponseDTO);

        resource.add(getToggleStatusLink());

        resource.add(getGetStatusLink());

        return resource;
    }

    @CrossOrigin("http://localhost:3000")
    @PostMapping(value = "/toggleStatus")
    @Operation(summary = "Toggle Status to switch mode from online/offline", description = "Toggle the status of the API to get data from swapi.dev or from local Datastructure.")
    public EntityModel<ResponseDTO> toggleStatus(@RequestParam("value") Boolean value){
        if(value == Constants.Status){
            throw new ApplicationExceptionHandler.ApplicationWrappedException(new SWException(ErrorCodes.STATUS_SAME.getErrorMessage()));
        }
        Constants.Status = value;
        String response = "Value set: " + value;
        EntityModel<ResponseDTO> resource = EntityModel.of(new ResponseDTO(response));

        resource.add(getFindLink());

        resource.add(getGetStatusLink());
        return resource;
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/getStatus")
    @Operation(summary = "Get the current status of the API", description = "Get the current status of the API to know if it is in online or offline mode.")
    public EntityModel<ResponseDTO> getStatus(){
        String response = "value is: " + Constants.Status;
        EntityModel<ResponseDTO> resource = EntityModel.of(new ResponseDTO(response));

        resource.add(getFindLink());

        resource.add(getToggleStatusLink());
        return resource;
    }

    private SwTypeConstants getEnumFromType(String type) throws SWException {
        if (!EnumUtils.isValidEnum(SwTypeConstants.class, type))
            throw new SWException(ErrorCodes.INVALID_TYPE.getErrorMessage());
        return SwTypeConstants.valueOf(type);
    }

    private Link getFindLink(){
        WebMvcLinkBuilder linkToToggleStatus = linkTo(methodOn(this.getClass()).find("People", "Luke Skywalker"));
        return linkToToggleStatus.withRel("find");
    }

    private Link getToggleStatusLink(){
        WebMvcLinkBuilder linkToGetStatus = linkTo(methodOn(this.getClass()).toggleStatus(!Constants.Status));
        return linkToGetStatus.withRel("toggle-status");
    }

    private Link getGetStatusLink(){
        WebMvcLinkBuilder linkToGetStatus = linkTo(methodOn(this.getClass()).getStatus());
        return linkToGetStatus.withRel("get-status");
    }
}
