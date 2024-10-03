package main.ui;

import lombok.RequiredArgsConstructor;
import main.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class UiController {

    private final CatalogService catalogService;

    /**
     * возврат старницы index со списком торговых каталогов
     * @param md
     * @return
     */
    @GetMapping("")
    public String index(Model md){

        md.addAttribute("catalogs", catalogService.getAllDto());

        return "index";
    }

}
