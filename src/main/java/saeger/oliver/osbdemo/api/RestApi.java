package saeger.oliver.osbdemo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saeger.oliver.osbdemo.api.request.CreateBindingRequest;
import saeger.oliver.osbdemo.api.request.ServiceInstanceRequest;
import saeger.oliver.osbdemo.api.response.GetCatalogResponse;
import saeger.oliver.osbdemo.model.Binding;
import saeger.oliver.osbdemo.model.ServiceInstance;
import saeger.oliver.osbdemo.model.ServiceOffering;
import saeger.oliver.osbdemo.service.BindingService;
import saeger.oliver.osbdemo.service.CatalogService;
import saeger.oliver.osbdemo.service.ServiceInstanceService;
import saeger.oliver.osbdemo.utility.StatusUtility;

import java.util.Collection;

@RestController
public class RestApi {

    private final CatalogService catalogService;
    private final ServiceInstanceService serviceInstanceService;
    private final BindingService bindingService;

    public RestApi(CatalogService catalogService, ServiceInstanceService serviceInstanceService,
                   BindingService bindingService) {
        this.catalogService = catalogService;
        this.serviceInstanceService = serviceInstanceService;
        this.bindingService = bindingService;
    }

    @GetMapping("/v2/catalog")
    public ResponseEntity<GetCatalogResponse> getCatalog() {
        Collection<ServiceOffering> serviceOfferings = catalogService.getCatalogs();
        var response = new GetCatalogResponse();
        response.setServices(serviceOfferings);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/v2/service_instances/{instanceId}")
    public ResponseEntity<Void> createServiceInstance(@PathVariable String instanceId,
                                                      @RequestParam(name = "accepts_incomplete", required = false) boolean acceptsIncomplete,
                                                      @RequestBody ServiceInstanceRequest request) {

        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setId(instanceId);
        serviceInstance.setOfferingId(request.getService_id());
        serviceInstance.setPlanId(request.getPlan_id());
        serviceInstance.setSpaceGuid(request.getSpace_guid());
        serviceInstance.setOrganizationGuid(request.getOrganizationGuid());

        CreationStatus creationStatus = serviceInstanceService.provisionServiceInstance(serviceInstance);

        return StatusUtility.handleCreationStatus(creationStatus);

    }

    @DeleteMapping("/v2/service_instances/{instanceId}")
    public ResponseEntity<Void> deleteServiceInstance(@PathVariable String instanceId,
                                                      @RequestParam("service_id") String serviceId,
                                                      @RequestParam("plan_id") String planId,
                                                      @RequestParam(name = "accepts_incomplete", required = false) boolean acceptsIncomplete) {

        DeletionStatus deletionStatus = serviceInstanceService.deprovisionServiceInstance(instanceId, serviceId, planId);
        return StatusUtility.handleDeletionStatus(deletionStatus);

    }

    @PutMapping("/v2/service_instances/{instanceId}/service_bindings/{bindingId}")
    public ResponseEntity<Void> createBinding(@PathVariable String instanceId,
                                              @PathVariable String bindingId,
                                              @RequestParam(name = "accepts_incomplete", required = false) boolean acceptsIncomplete,
                                              @RequestBody CreateBindingRequest createBindingRequest) {

        Binding binding = new Binding();
        binding.setId(bindingId);
        binding.setServiceInstanceId(instanceId);
        binding.setServiceId(createBindingRequest.getService_id());
        binding.setPlanId(createBindingRequest.getPlan_id());

        CreationStatus creationStatus = bindingService.createBinding(binding);

        return StatusUtility.handleCreationStatus(creationStatus);

    }

    @DeleteMapping("/v2/service_instances/{instanceId}/service_bindings/{bindingId}")
    public ResponseEntity<Void> deleteBinding(@PathVariable String instanceId,
                                              @PathVariable String bindingId,
                                              @RequestParam("service_id") String serviceId,
                                              @RequestParam("plan_id") String planId,
                                              @RequestParam(name = "accepts_incomplete", required = false) boolean acceptsIncomplete) {

        DeletionStatus deletionStatus = bindingService.unbind(instanceId, bindingId, serviceId, planId);
        return StatusUtility.handleDeletionStatus(deletionStatus);

    }

}
