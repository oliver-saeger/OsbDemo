package saeger.oliver.osbdemo.service;

import org.springframework.stereotype.Service;
import saeger.oliver.osbdemo.api.CreationStatus;
import saeger.oliver.osbdemo.api.DeletionStatus;
import saeger.oliver.osbdemo.model.Binding;

import java.util.HashMap;
import java.util.Map;

@Service
public class BindingService {

    private final Map<String, Binding> bindings = new HashMap<>();

    public CreationStatus createBinding(Binding binding) {

        if (bindings.containsKey(binding.getId())) {
            if (hasConflicts(binding)) {
                return CreationStatus.CONFLICT;
            }

            return CreationStatus.ALREADY_EXISTS;
        } else {
            bindings.put(binding.getId(), binding);
            return CreationStatus.CREATED;
        }

    }

    public DeletionStatus unbind(String instanceId, String bindingId, String serviceId, String planId) {

        if (!bindings.containsKey(bindingId)) {
            return DeletionStatus.DOES_NOT_EXIST;
        }

        Binding binding = bindings.get(bindingId);

        if (isDeletionDataCorrect(instanceId, serviceId, planId, binding)) {
            bindings.remove(bindingId);
            return DeletionStatus.DELETED;
        }

        return DeletionStatus.WRONG_DATA;

    }

    private boolean isDeletionDataCorrect(String instanceId, String serviceId, String planId, Binding binding) {
        boolean isInstanceIdCorrect = binding.getServiceInstanceId().equals(instanceId);
        boolean isServiceIdCorrect = binding.getServiceId().equals(serviceId);
        boolean isPlanIdCorrect = binding.getPlanId().equals(planId);

        return isServiceIdCorrect && isPlanIdCorrect && isInstanceIdCorrect;
    }

    private boolean hasConflicts(Binding binding) {

        boolean isOnlyViableId = "myServiceInstanceId".equals(binding.getServiceInstanceId());
        boolean doesOfferingExistInService = "myServiceId".equals(binding.getServiceId());
        boolean doesPlanExistInOffering = "myServicePlanId".equals(binding.getPlanId());

        return !(doesOfferingExistInService && doesPlanExistInOffering && isOnlyViableId);
    }
    
}
