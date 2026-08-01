package com.v1.service.home.home.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EChart {

    private List<String> names = new ArrayList<>();
    private List<String> values = new ArrayList<>();

}
