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

    List<DTO> dtoList;

    List<T> tList;

    T t;

    DTO dto;
}
