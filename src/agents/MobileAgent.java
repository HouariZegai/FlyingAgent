package agents;

import information.AllInformation;
import information.OSInformation;
import information.RawInformation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;

public class MobileAgent extends Agent {

    public static final String NAME = "MobileAgent";
    private static final int SCAN = 177;
    private static final int ONE = 179;
    private AID stableAgent = new AID(MainAgent.NAME, AID.ISLOCALNAME);
    private Location currentLocation;
    private int status = 0;
    private List<OSInformation> osInformation = new ArrayList<>();
    private ServeMovingMessages movingMessagesBehaviour = new ServeMovingMessages();
    private jade.util.leap.List allLocations;
    private int index = 0;

    @Override
    protected void setup() {
        addBehaviour(movingMessagesBehaviour);
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent done.");
    }

    @Override
    protected void afterMove() {
        if (status == ONE)
            sendBasicInformation();
        else if (status == SCAN) {
            System.out.println("After moving, in scan part");
            OSInformation os = new OSInformation();
            osInformation.add(os);
            if (index == allLocations.size() - 1) {
                System.out.println(osInformation.toString());
                addBehaviour(movingMessagesBehaviour);
                status = ONE;
            } else
                doMove((Location) allLocations.get(++index));
        }

    }

    private void sendBasicInformation() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(AllInformation.getInstance().toJson());
        message.addReceiver(stableAgent);
        send(message);
    }

    private void handleMovingRequest(ACLMessage message) {
        try {
            if (currentLocation == message.getContentObject()) {
                sendBasicInformation();
            } else {
                Location location = (Location) message.getContentObject();
                currentLocation = location;
                doMove(location);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    private void handleRawInfo(ACLMessage message) {
        ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
        String moreInfo = new RawInformation().toString();
        System.out.println(moreInfo);
        msg.setContent(moreInfo);
        msg.addReceiver(stableAgent);
        msg.setConversationId(message.getConversationId());
        send(msg);
    }

    private void handleVisitAll(ACLMessage message) {
        System.out.println("Handle Visit all was called");
        removeBehaviour(movingMessagesBehaviour);
        status = SCAN;
        osInformation.clear();
        try {
            allLocations = (jade.util.leap.List) message.getContentObject();
            System.out.println("All location was initialized, its length was " + allLocations.size());
        } catch (UnreadableException e) {
            e.printStackTrace();
            System.out.println("All location was not initialized, expetion" );
        }
        doMove((Location) allLocations.get(index));
    }

    public class ServeMovingMessages extends CyclicBehaviour {

        private MessageTemplate template = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)), MessageTemplate.MatchPerformative(ACLMessage.CFP));

        @Override
        public void action() {
            System.out.println("Action in ServeMovingMessages called");
            ACLMessage message = receive();
            if (message != null) {
                switch (message.getPerformative()) {
                    case ACLMessage.QUERY_IF:
                        status = ONE;
                        handleMovingRequest(message);
                        break;
                    case ACLMessage.REQUEST:
                        status = ONE;
                        handleRawInfo(message);
                        break;
                    case ACLMessage.CFP:
                        status = SCAN;
                        System.out.println("Message was CFP");
                        handleVisitAll(message);
                        break;
                }
            } else block();
        }
    }
}
