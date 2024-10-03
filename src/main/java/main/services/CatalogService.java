package main.services;

import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.CatalogDTO;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    //region FIELDS
    private final CatalogRepository catalogRepository;
    //endregion

    /**
     * Метод возвращает все каталоги без пометки удаления и сортирует их по порядку ранга
     * @return
     */
    public List<Catalog> getAll(){
        return catalogRepository.findByDeleted(0).stream()
                .filter(c -> c.getDeleted() == 0)
                .sorted((c1,c2) -> Integer.compare(c1.getSorting(), c2.getSorting()))
                .toList();
    }

    /**
     * Метод возращает все каталоги, сортируя их по рангу в виде DTO,
     * при этом поток бит изображения декодируется в строку
     * @return
     */
    public List<CatalogDTO> getAllDto(){
        List<Catalog> catalogs = catalogRepository.findByDeleted(0).stream()
                .filter(c -> c.getDeleted() == 0)
                .sorted((c1,c2) -> Integer.compare(c1.getSorting(), c2.getSorting()))
                .toList();

        List<CatalogDTO> catalogDTOs = new ArrayList<>();

        for (Catalog catalog: catalogs) {
            CatalogDTO catalogDTO = new CatalogDTO();
            catalogDTO.setId(catalog.getId());
            catalogDTO.setName(catalog.getName());
            catalogDTO.setTextButton(catalog.getTextButton());
            catalogDTO.setImage64(Base64.getEncoder().encodeToString(catalog.getImage64()));
            catalogDTOs.add(catalogDTO);
        }

        return catalogDTOs;
    }

    /**
     * Метод получает список каталогов для загрузки, сопоставляет их по идентификатору из 1с
     * возвращает результат статуса загрузки
     * @param catalogs
     * @return
     */
    public StatusLoad createUpdateCatalogs(List<Catalog> catalogs){
        StatusLoad statusLoad = new StatusLoad();

        for (Catalog catalog: catalogs) {
            Catalog catalogInDb = catalogRepository.findById1c(catalog.getId1c());

            LoadLine loadLine = new LoadLine(catalog.getId1c());

            if (catalogInDb != null){
                catalog.setId(catalogInDb.getId());
            }else {
                loadLine.setStatus("Загружен");
            }

            catalogRepository.save(catalog);
            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");

            statusLoad.addLog(loadLine);
        }

        return statusLoad;
    }
}
