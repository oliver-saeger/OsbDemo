package saeger.oliver.osbdemo.api.response;

import lombok.Data;
import saeger.oliver.osbdemo.model.ServiceOffering;

import java.util.Collection;

@Data
public class GetCatalogResponse {
    private Collection<ServiceOffering> services;
}
