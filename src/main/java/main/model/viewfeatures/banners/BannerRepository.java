package main.model.viewfeatures.banners;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, String> {

    Banner findById1c(String id1c);

    Banner findByName(String name);
}