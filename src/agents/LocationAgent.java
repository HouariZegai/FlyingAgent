package agents;

import behaviours.GetAvailableLocationsBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class LocationAgent extends Agent {

    static final String TYPE = "LOCATION";
    static final String NAME = "LocationAgent";
    private ArrayList<ContainerID> availableContainers = new ArrayList<>();
    private List location;

    @Override
    protected void setup() {

        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(MobilityOntology.getInstance());

        registerToDF();
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

    public void setLocations(List items) {
        this.location = items;
        System.out.println("Updating Location " + items.toString());
    }

    class ServeLocationRequests extends CyclicBehaviour {

        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

        @Override
        public void action() {
            System.out.println("2. ServeLocationRequests arrived Message");
            ACLMessage message = receive(template);
            //addBehaviour(new GetAvailableLocationsBehaviour((LocationAgent) myAgent));
            if (message != null) {
                responseWithLocations(message);
            } else {
                block();
            }
        }

        private void responseWithLocations(ACLMessage message) {
            ACLMessage reply = message.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            try {
                reply.setContentObject((Serializable) location);
            } catch (IOException e) {
                e.printStackTrace();
            }
            send(reply);
            System.out.println("3. ServeLocationRequest message replied");
        }
    }

}
