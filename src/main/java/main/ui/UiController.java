package main.ui;

import lombok.RequiredArgsConstructor;
import main.fileTransfer.FileUploader;
import main.model.catalog.Catalog;
import main.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class UiController {

    private final CatalogService catalogService;
    private final FileUploader fileUploader;

    @GetMapping("")
    public String index(Model md){
        List<Catalog> catalogs = catalogService.getAll();

//        for (Catalog cat: catalogs) {
//            cat.setImagePath(fileUploader.getGlobalResPath()+cat.getImagePath());
//        }
        md.addAttribute("fu", fileUploader.getGlobalResPath());
        md.addAttribute("catalogs", catalogService.getAll());

        return "index";
    }

}
