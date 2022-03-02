package saeger.oliver.osbdemo.model;

import lombok.Data;

@Data
public class ServiceInstance {

    private String id;
    private String offeringId;
    private String planId;
    private String spaceGuid;
    private String organizationGuid;
}
