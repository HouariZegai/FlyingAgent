package agents;

import behaviours.GetAvailableLocationsBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class ReceiverAgent extends Agent {

    public static final String NAME = "WaiterAgent";
    private List locationList;
    private AID mobileAgent;

    @Override
    protected void setup() {
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(MobilityOntology.getInstance());

        addBehaviour(new GetAvailableLocationsBehaviour(this));
        addBehaviour(new OneReceiveBehavior());
    }


    public void updateLocations(List items) {
        System.out.println("from receiver "+ (items));
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.addReceiver(new AID("Service-Agent", AID.ISLOCALNAME));
        try {
            message.setContentObject((Serializable) items.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(message);
        this.locationList = items;
    }

    public List getLocationList() {
        return locationList;
    }

    class OneReceiveBehavior extends CyclicBehaviour {

        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        @Override
        public void action() {
            ACLMessage message = receive(template);
            if (message != null) {
                handleMessage(message.getContent());
                mobileAgent = message.getSender();
                addBehaviour(new AskMoreBehavior());
            } else {
                block();
            }
        }

        private void handleMessage(String jsonResponse) {
            System.out.println(jsonResponse);
        }
    }

    private class AskMoreBehavior extends Behaviour {
        private static final int ASK = 0;
        private static final int RESPONSE = 1;
        private static final int DONE = 2;
        private int status = ASK;
        private MessageTemplate template;

        @Override
        public void action() {
            switch (status) {
                case ASK:
                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                    message.setConversationId(String.valueOf(System.currentTimeMillis()));
                    message.addReceiver(mobileAgent);
                    template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE),
                            MessageTemplate.MatchConversationId(message.getConversationId()));
                    send(message);
                    status = RESPONSE;
                    break;
                case RESPONSE:
                    ACLMessage receivedMessage = receive(template);
                    if (receivedMessage != null) {
                        handleRawData(receivedMessage.getContent());
                    } else {
                        block();
                    }
                    break;
            }
        }

        private void handleRawData(String rawJson) {
            System.out.println(rawJson);
            status = DONE;
        }

        @Override
        public boolean done() {
            return status == DONE;
        }
    }
}
