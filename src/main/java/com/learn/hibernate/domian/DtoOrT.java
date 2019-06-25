package com.learn.hibernate.domian;

import lombok.Data;

import java.util.List;

/**
 * @author lee
 * @param <DTO>
 * @param <T>
 */
@Data
public class DtoOrT<DTO,T> {

    private List<DTO> dtoList;

    private List<T> tList;

    private T t;

    private DTO dto;
}
