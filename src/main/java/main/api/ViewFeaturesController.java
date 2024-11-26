package main.api;

import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.model.viewfeatures.banners.Banner;
import main.services.ViewFeaturesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ViewFeaturesController{

    //region FIELDS
    private final ViewFeaturesService viewFeaturesService;
    //endregion

    //region POSTS
    @PostMapping("banners/update")
    public ResponseEntity updateBanners(@RequestBody List<Banner> banners){
        StatusLoad statusLoad = viewFeaturesService.updateBanners(banners);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @GetMapping("banner/main/app")
    public List<String> mobileAppMainBanner(){
        return viewFeaturesService.getMainBannerMobileApps();
    }
    //endregion

}
