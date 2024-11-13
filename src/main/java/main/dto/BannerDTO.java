package main.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BannerDTO {
    private String id;
    private String name;
    private List<String> images = new ArrayList<String>();
}
