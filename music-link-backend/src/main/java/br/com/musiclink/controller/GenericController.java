package br.com.musiclink.controller;

import br.com.serviceframework.domain.entity.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public abstract class GenericController<T, DTO> {

    @GetMapping
    public abstract DTO findById();

    @GetMapping
    public abstract List<DTO> findAll();

    @PostMapping
    public abstract DTO save();

    @PutMapping
    public abstract DTO update();

    @DeleteMapping
    public abstract void delete();
}
