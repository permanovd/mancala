package com.permanovd.gamesessionservice.infrastructure.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.permanovd.gamesessionservice.domain.BoardState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class JpaConverterBoardStateToJson implements AttributeConverter<BoardState, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(BoardState meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public BoardState convertToEntityAttribute(String dbData) {
        try {
            BoardState result = objectMapper.readValue(dbData, BoardState.class);
            return result;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}