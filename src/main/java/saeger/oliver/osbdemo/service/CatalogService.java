package saeger.oliver.osbdemo.service;

import org.springframework.stereotype.Service;
import saeger.oliver.osbdemo.model.ServiceOffering;
import saeger.oliver.osbdemo.model.ServicePlan;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CatalogService {

    public Collection<ServiceOffering> getCatalogs() {
        Collection<ServiceOffering> serviceOfferings = new ArrayList<>();

        ServiceOffering serviceOffering = getServiceOffering();
        serviceOfferings.add(serviceOffering);

        return serviceOfferings;
    }

    private ServiceOffering getServiceOffering() {
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setBindable(true);
        serviceOffering.setDescription("this is my service");
        serviceOffering.setId("myServiceId");
        serviceOffering.setName("myServiceName");

        Collection<ServicePlan> servicePlans = new ArrayList<>();

        ServicePlan servicePlan = getServicePlan();
        servicePlans.add(servicePlan);
        serviceOffering.setPlans(servicePlans);
        return serviceOffering;
    }

    private ServicePlan getServicePlan() {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("myServicePlanName");
        servicePlan.setId("myServicePlanId");
        servicePlan.setDescription("this is my service plan");
        return servicePlan;
    }
}
