package saeger.oliver.osbdemo.api.request;

import lombok.Data;

@Data
public class CreateBindingRequest {

    private String service_id;
    private String plan_id;

}
