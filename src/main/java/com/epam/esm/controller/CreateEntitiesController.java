package com.epam.esm.controller;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.util.Import1000;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entity")
public class CreateEntitiesController {
    private final Import1000 import1000;
    @GetMapping("/create")
    public ResponseEntity<String> create(){
        try {
            import1000.parseFileWithNames();
            import1000.parseFileWithTags();
            import1000.createCertificatesWithTags();
        } catch (Exception exception) {
            throw new ApiEntityNotFoundException("Something went wrong!!!");
        }
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }
}
