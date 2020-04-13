package com.bharat.janmat.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FileBasedRepository<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedRepository.class);
    private final ObjectMapper objectMapper;
    private final Class modelType;
    private final String dataFileName;
    private final Path dataFilePath;

    public FileBasedRepository(ObjectMapper objectMapper, Class<M> clazz) {
        this.objectMapper = objectMapper;
        modelType = clazz;
        LOGGER.debug("FileBasedRepository for type {} created", clazz.getTypeName());
        if (modelType.getName().equals("com.bharat.janmat.model.Mat")) {
            dataFileName = "janmat-data.dat";
        } else {
            dataFileName = "common-data.dat";
        }
        File file = new File(dataFileName);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                LOGGER.debug("Data file initialized for: {} - {}", modelType, newFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dataFilePath = Paths.get(dataFileName);
    }

    public Long getCount() {
        try (Stream<String> lines = Files.lines(dataFilePath)) {
            return lines.count();
        } catch (Exception e) {
            LOGGER.error("Error while reading count for type: {}", modelType, e);
            return 0L;
        }
    }

    public void save(Object entity) {
        try {
            String matAsJson = objectMapper.writeValueAsString(entity) + "\n";
            Files.write(dataFilePath, matAsJson.getBytes(), StandardOpenOption.APPEND);
            LOGGER.debug("Record written to file: " + matAsJson);
        } catch (Exception e) {
            LOGGER.error("Error while reading count for type: {}", modelType, e);
        }
    }

    public List findAll() {

        try (Stream<String> lines = Files.lines(dataFilePath)) {
            return lines.map(record -> {
                try {
                    return objectMapper
                            .readValue(record, modelType);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull)
                    .collect(toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while reading count for type: " + modelType.getName(), e);
        }
    }
}