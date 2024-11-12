package main.services;

import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.goods.characs.Charac;
import main.model.viewfeatures.banners.Banner;
import main.model.viewfeatures.banners.BannerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewFeaturesService {

    //region FIELDS
    private final BannerRepository bannerRepository;
    //endregion

    //region METHODS
    public StatusLoad updateBanners(List<Banner> banners){
        StatusLoad statusLoad = new StatusLoad();

        for (Banner banner : banners) {
            Banner banerFromDB = bannerRepository.findById1c(banner.getId1c());

            LoadLine loadLine = new LoadLine(banner.getId1c());

            if (banerFromDB != null) {
                banner.setId(banerFromDB.getId());
            }else {
                loadLine.setStatus("Загружен");
            }
            bannerRepository.save(banner);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");

            statusLoad.addLog(loadLine);
        }
        return statusLoad;
    }

    public Banner findById1c(String id1c) {
        return bannerRepository.findById1c(id1c);
    }
    //endregion
}
