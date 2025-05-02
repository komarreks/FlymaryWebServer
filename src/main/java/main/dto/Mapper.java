package main.dto;

import lombok.RequiredArgsConstructor;
import main.model.catalog.CatalogNode;
import main.model.catalog.nodechilddata.NodeProduct;
import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.images.Image;
import main.services.ProductSevice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ProductSevice productSevice;

    public List<NodeDTO> transferToNodesDTO(List<CatalogNode> catalogNodes){
        List<NodeDTO> nodeDTOs = new ArrayList<>();

        for (CatalogNode catalogNode : catalogNodes) {
            NodeDTO nodeDTO = new NodeDTO();
            nodeDTO.setName(catalogNode.getName());
            nodeDTO.setId(catalogNode.getId1c());
            nodeDTOs.add(nodeDTO);
        }

        return nodeDTOs;
    }

    public List<ProductDTO> transferToProductsDTO(List<NodeProduct> nodeProducts){
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (NodeProduct nodeProduct : nodeProducts) {

            if (!nodeProduct.getProduct().isVisible())continue;

            List<Charac> characs = new ArrayList<>(productSevice.getCharacs(nodeProduct.getProduct()));

            if (characs.isEmpty()) {
                if (Objects.equals(nodeProduct.getProduct().getPrice(), BigDecimal.ZERO))continue;
                if (Objects.equals(nodeProduct.getProduct().getCount(), BigDecimal.ZERO))continue;
            }

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(nodeProduct.getProduct().getId());
            productDTO.setName(nodeProduct.getProduct().getName());
            productDTO.setPrice(nodeProduct.getProduct().getPrice().doubleValue());
            productDTO.setFilterNode(nodeProduct.getNode().getId1c());
            productDTO.setCount(nodeProduct.getProduct().getCount().doubleValue());
            productDTO.setImageUrl(nodeProduct.getProduct().getImages().stream().map(image -> {
                return image.getName();
            }).toList());

            productDTO.setCharacTDOs(new ArrayList<>());

            if (characs.size() > 1){
                Collections.sort(characs, (c1, c2)-> Integer.compare(c2.getValue(), c1.getValue()));
            }

            for (Charac charac: characs){

                if (!charac.isVisible())continue;
                if (Objects.equals(charac.getPrice(), BigDecimal.ZERO))continue;
                if (Objects.equals(charac.getCount(), BigDecimal.ZERO))continue;

                CharacTDO characTDO = new CharacTDO();

                characTDO.setId(charac.getId());
                characTDO.setProductId(nodeProduct.getProduct().getId());
                characTDO.setName(charac.getName());
                characTDO.setImageUrl(charac.getImages().stream().map(
                        image -> image.getName()
                ).toList());
                characTDO.setPrice(charac.getPrice().doubleValue());
                characTDO.setCount(charac.getCount().doubleValue());

                productDTO.getCharacTDOs().add(characTDO);
            }

            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    public ProductDTO transferToProductsDTO(Product product){
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice().doubleValue());
        productDTO.setCount(product.getCount().doubleValue());
        productDTO.setFilterNode("");
        productDTO.setImageUrl(product.getImages().stream().map(Image::getName).toList());

        List<Charac> characs = productSevice.getCharacs(product);

        if (characs != null){
            for (Charac charac: characs){
                if (!charac.isVisible())continue;
                if (Objects.equals(charac.getPrice(), BigDecimal.ZERO))continue;
                if (Objects.equals(charac.getCount(), BigDecimal.ZERO))continue;
                CharacTDO characTDO = new CharacTDO();
                characTDO.setId(charac.getId());
                characTDO.setProductId(product.getId());
                characTDO.setName(charac.getName());
                characTDO.setPrice(charac.getPrice().doubleValue());
                characTDO.setCount(charac.getCount().doubleValue());
                characTDO.setImageUrl(charac.getImages().stream().map(Image::getName).toList());
                productDTO.getCharacTDOs().add(characTDO);
            }
        }

        return productDTO;
    }
}
