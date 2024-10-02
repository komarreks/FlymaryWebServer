package main.services;

import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public List<Catalog> getAll(){
        return catalogRepository.findByDeleted(0).stream().filter(c -> c.getDeleted() == 0).toList();
    }

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
