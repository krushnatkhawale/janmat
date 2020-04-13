package com.bharat.janmat.repositories;

import com.bharat.janmat.model.Mat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MatRepository extends FileBasedRepository<Mat> {

    public MatRepository(ObjectMapper objectMapper) {
        super(objectMapper, Mat.class);
    }
}
