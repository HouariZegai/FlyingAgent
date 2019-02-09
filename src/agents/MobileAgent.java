package agents;

import information.AllInformation;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class MobileAgent extends Agent {

    private AID stableAgent = new AID(ReceiverAgent.NAME, AID.ISLOCALNAME);

    @Override
    protected void setup() {
        addBehaviour(new ServeMovingMessages());
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent done.");
    }

    @Override
    protected void afterMove() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(AllInformation.getInstance().toJson());
        message.addReceiver(stableAgent);
        send(message);
    }

    public class ServeMovingMessages extends CyclicBehaviour {

        private MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.CFP);

        @Override
        public void action() {
            ACLMessage message = receive(template);
            if (message != null) {
                try {
                    Location location = (Location) message.getContentObject();
                    System.out.println(location.toString());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            } else block();
        }
    }
}
