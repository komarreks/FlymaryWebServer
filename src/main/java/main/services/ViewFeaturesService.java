package main.services;

import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.BannerDTO;
import main.model.images.Image;
import main.model.viewfeatures.banners.Banner;
import main.model.viewfeatures.banners.BannerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewFeaturesService {

    //region FIELDS
    private final BannerRepository bannerRepository;
    //endregion

    //region METHODS
    /**
     * Метод загружает или обновляет баннеры, сопоставля их по id из 1С
     * @param banners
     * @return
     */
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

    /**
     * Метод возвращает баннер из репозитория по id из 1с
     * @param id1c
     * @return
     */
    public Banner findById1c(String id1c) {
        return bannerRepository.findById1c(id1c);
    }

    /**
     * Метод возвращает главный баннер для мобильного приложения со списком изображений
     * @return
     */
    public BannerDTO getMainBannerMobileApps(){
        Banner banner = bannerRepository.findByName("MobileApps");

        BannerDTO bannerDTO = new BannerDTO();

        if (banner != null){
            bannerDTO.setId(banner.getId());
            bannerDTO.setName(banner.getName());

            List<String> images = new ArrayList<>();

            for (Image image: banner.getImages()){
                images.add(image.getName());
            }

            bannerDTO.setImages(images);
        }

        return bannerDTO;
    }
    //endregion
}
