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
import org.apache.commons.lang3.EnumUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final OfflineService offlineService;
    private final OnlineService onlineService;

    public ApiController(OfflineService offlineService, OnlineService onlineService){
        this.offlineService = offlineService;
        this.onlineService = onlineService;
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/find")
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
