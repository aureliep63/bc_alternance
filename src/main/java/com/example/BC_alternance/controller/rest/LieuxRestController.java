package com.example.BC_alternance.controller.rest;

import com.example.BC_alternance.dto.LieuxDto;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.service.LieuxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lieux")
@Validated
public class LieuxRestController {

    private LieuxService lieuxService;

    public LieuxRestController(LieuxService lieuxService){
        this.lieuxService = lieuxService;
    }

    @GetMapping("")
    public List<LieuxDto> getAllLieux() {
        return lieuxService.getAllLieux();
    }

    @GetMapping("/{id}")
    public LieuxDto getLieuxById(@PathVariable Long id) {
        return lieuxService.getLieuxById(id);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Lieux saveLieux(@Valid @RequestBody LieuxDto lieuxDto) {
        return lieuxService.saveLieux(lieuxDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteLieux(@PathVariable Long id) {
    lieuxService.deleteLieux(id);
    }

    @PutMapping("/{id}")
    public Lieux updateLieux(@PathVariable Long id,@Valid @RequestBody LieuxDto lieuxDto) {
        lieuxDto.setId(id);
        return lieuxService.saveLieux(lieuxDto);
    }
}