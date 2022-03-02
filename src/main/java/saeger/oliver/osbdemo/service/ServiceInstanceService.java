package saeger.oliver.osbdemo.service;

import org.springframework.stereotype.Service;
import saeger.oliver.osbdemo.api.CreationStatus;
import saeger.oliver.osbdemo.api.DeletionStatus;
import saeger.oliver.osbdemo.model.ServiceInstance;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceInstanceService {

    private final Map<String, ServiceInstance> provisionedServiceInstances = new HashMap<>();

    public CreationStatus provisionServiceInstance(ServiceInstance serviceInstance) {

        if (provisionedServiceInstances.containsKey(serviceInstance.getId())) {
            if (hasConflicts(serviceInstance)) {
                return CreationStatus.CONFLICT;
            }

            return CreationStatus.ALREADY_EXISTS;
        } else {
            provisionedServiceInstances.put(serviceInstance.getId(), serviceInstance);
            return CreationStatus.CREATED;
        }

    }

    private boolean hasConflicts(ServiceInstance serviceInstance) {

        boolean isOnlyViableId = "myServiceInstanceId".equals(serviceInstance.getId());
        boolean doesOfferingExistInService = "myServiceId".equals(serviceInstance.getOfferingId());
        boolean doesPlanExistInOffering = "myServicePlanId".equals(serviceInstance.getPlanId());

        return !(isOnlyViableId && doesOfferingExistInService && doesPlanExistInOffering);
    }

    public DeletionStatus deprovisionServiceInstance(String id, String serviceId, String planId) {

        if (!provisionedServiceInstances.containsKey(id)) {
            return DeletionStatus.DOES_NOT_EXIST;
        }

        ServiceInstance instance = provisionedServiceInstances.get(id);

        if (isDeletionDataCorrect(serviceId, planId, instance)) {
            provisionedServiceInstances.remove(id);
            return DeletionStatus.DELETED;
        }

        return DeletionStatus.WRONG_DATA;

    }

    private boolean isDeletionDataCorrect(String serviceId, String planId, ServiceInstance instance) {
        return instance.getOfferingId().equals(serviceId) && instance.getPlanId().equals(planId);
    }

}
