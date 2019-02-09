package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class ReceiverAgent extends Agent {

    public static final String NAME = "WaiterAgent";
    private List locationList;
    private AID mobileAgent;

    @Override
    protected void setup() {

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addBehaviour(new GetLocationsBehavior());
        addBehaviour(new OneReceiveBehavior());
    }


    public void updateLocations(List items) {
        System.out.println("from receiver " + (items));
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
                try {
                    System.out.println("From Receiver" + message.getContentObject().toString());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

                //mobileAgent = message.getSender();
                //addBehaviour(new AskMoreBehavior());
            } else {
                block();
            }
        }

        private void handleMessage(String jsonResponse) {
            System.out.println(jsonResponse);
        }
    }

    class GetLocationsBehavior extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(LocationAgent.TYPE);
            serviceDescription.setName(LocationAgent.NAME);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, agentDescription);
                if (result.length == 1) {
                    message.addReceiver(result[0].getName());
                    send(message);
                    System.out.println("1. Receiver message sent");
                } else {
                    System.out.println("Could not reach Location agent");
                }
            } catch (FIPAException e) {
                e.printStackTrace();
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
