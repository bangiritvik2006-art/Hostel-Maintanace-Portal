package com.hostelfix.controller;
import com.hostelfix.dto.StatusUpdate;
import com.hostelfix.model.*;
import com.hostelfix.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api")
public class RequestController {
  private final RequestService service;
  public RequestController(RequestService s){service=s;}
  @GetMapping("/requests") public List<MaintenanceRequest> all(@RequestParam(required=false) String search,@RequestParam(required=false) String status,@RequestParam(required=false) String category){return service.findAll(search,status,category);}
  @GetMapping("/requests/{id}") public MaintenanceRequest one(@PathVariable Long id){return service.findById(id);}
  @PostMapping("/requests") @ResponseStatus(HttpStatus.CREATED) public MaintenanceRequest create(@Valid @RequestBody MaintenanceRequest r){return service.create(r);}
  @PutMapping("/requests/{id}") public MaintenanceRequest update(@PathVariable Long id,@Valid @RequestBody MaintenanceRequest r){return service.update(id,r);}
  @PatchMapping("/requests/{id}/status") public MaintenanceRequest status(@PathVariable Long id,@Valid @RequestBody StatusUpdate body){return service.updateStatus(id,body.status());}
  @PatchMapping("/requests/{id}/assign/{staffId}") public MaintenanceRequest assign(@PathVariable Long id,@PathVariable Long staffId){return service.assign(id,staffId);}
  @DeleteMapping("/requests/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
  @GetMapping("/dashboard/stats") public Map<String,Long> stats(){return service.stats();}
}
