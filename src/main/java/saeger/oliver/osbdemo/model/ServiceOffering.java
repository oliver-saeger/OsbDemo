package saeger.oliver.osbdemo.model;

import lombok.Data;

import java.util.Collection;

@Data
public class ServiceOffering {

    // only required fields for now
    private String name;
    private String id;
    private String description;
    private boolean bindable;
    private Collection<ServicePlan> plans;

}
