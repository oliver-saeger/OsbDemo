package saeger.oliver.osbdemo.api.request;

import lombok.Data;

@Data
public class ServiceInstanceRequest {
    String service_id;
    String plan_id;
    String organizationGuid;
    String space_guid;
}
