package main.dto;

import main.model.catalog.CatalogNode;
import main.model.catalog.nodechilddata.NodeProduct;
import main.model.goods.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

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

//    public List<ProductDTO> transferToProductsDTO(List<Product> products){
//        List<ProductDTO> productDTOS = new ArrayList<>();
//
//        for (Product product : products) {
//            ProductDTO productDTO = new ProductDTO();
//            productDTO.setId(product.getId());
//            productDTO.setName(product.getName());
//            // TODO: исправить цену на получение верно
//            productDTO.setPrice(100.0);
//            productDTO.setImageUrl(product.getImages().stream().map(image -> {
//                return image.getName();
//            }).toList());
//
//            productDTOS.add(productDTO);
//        }
//
//        return productDTOS;
//    }

    public List<ProductDTO> transferToProductsDTO(List<NodeProduct> nodeProducts){
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (NodeProduct nodeProduct : nodeProducts) {

            if (!nodeProduct.getProduct().isVisible())continue;

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(nodeProduct.getProduct().getId());
            productDTO.setName(nodeProduct.getProduct().getName());
            productDTO.setPrice(100.0);
            productDTO.setFilterNode(nodeProduct.getNode().getId1c());
            productDTO.setImageUrl(nodeProduct.getProduct().getImages().stream().map(image -> {
                return image.getName();
            }).toList());
            productDTOS.add(productDTO);

        }

        return productDTOS;
    }
}
