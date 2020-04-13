package com.bharat.janmat.services;

import com.bharat.janmat.model.Mat;
import com.bharat.janmat.repositories.MatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MatService {

    private final MatRepository matRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public MatService(MatRepository matRepository) {
        this.matRepository = matRepository;
    }

    public void save(Mat mat) {

        Long count = matRepository.getCount();
        mat.setId(count + 1);
        mat.setPostedOn(ZonedDateTime.now());
        matRepository.save(mat);
    }

    public List<Mat> findAll() {
        return matRepository.findAll();
    }
}