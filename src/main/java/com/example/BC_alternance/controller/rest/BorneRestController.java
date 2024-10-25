package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.BorneDto;
import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.service.BorneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/bornes")
@Validated
public class BorneRestController {

    private BorneService borneService;
    public BorneRestController(BorneService borneService){
        this.borneService = borneService;
    }

    @GetMapping("")
    public List<BorneDto> getAllBornes() {
        return borneService.getAllBornes();
    }

    @GetMapping("/{id}")
    public BorneDto getBorneById(@PathVariable Long id) {
        return borneService.getBorneById(id);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Borne saveBorne(@Valid @RequestBody BorneDto borneDto) {
        return borneService.saveBorne(borneDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBorne(@PathVariable Long id) {
        borneService.deleteBorne(id);
    }

    @PutMapping("/{id}")
    public Borne updateBorne(@PathVariable Long id,@Valid @RequestBody BorneDto borneDto) {
        borneDto.setId(id);
        return borneService.saveBorne(borneDto);
    }
}

