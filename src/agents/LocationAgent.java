package agents;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.AddedContainer;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.domain.introspection.RemovedContainer;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Map;

public class LocationAgent extends Agent {

    public static final String TYPE = "LOCATION";
    public static final String NAME = "LocationAgent";
    private ArrayList<ContainerID> availableContainers = new ArrayList<>();

    @Override
    protected void setup() {

        registerToDF();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AMSSubscriber subscriber = new AMSSubscriber() {
            protected void installHandlers(Map handlers) {


                EventHandler addedHandler = (EventHandler) event -> {
                    AddedContainer addedContainer = (AddedContainer) event;
                    availableContainers.add(addedContainer.getContainer());
                    System.out.println("From Location" +availableContainers.toString());
                };
                handlers.put(IntrospectionVocabulary.ADDEDCONTAINER, addedHandler);

                EventHandler removedHandler = (EventHandler) event -> {
                    RemovedContainer removedContainer = (RemovedContainer) event;
                    ArrayList<ContainerID> temp = new ArrayList<>(availableContainers);
                    for (ContainerID container : temp) {
                        if (container.getID().equalsIgnoreCase(removedContainer.getContainer().getID()))
                            availableContainers.remove(container);

                    }
                    System.out.println("From Location" + availableContainers.toString());
                };
                handlers.put(IntrospectionVocabulary.REMOVEDCONTAINER, removedHandler);
            }
        };
        addBehaviour(subscriber);
        addBehaviour(new ServeLocationRequests());
    }

    private void registerToDF() {
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(TYPE);
        serviceDescription.setName(NAME);
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
            System.out.println("Registration Done");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    class ServeLocationRequests extends CyclicBehaviour {

        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.CFP);

        @Override
        public void action() {
            System.out.println("OneReceiveBehavior started");
            ACLMessage message = receive();
            if (message != null) {
                responseWithLocations();
            } else {
                block();
            }
        }

        private void responseWithLocations() {
            System.out.println("From Location Message " + availableContainers.toString());
        }
    }

}
