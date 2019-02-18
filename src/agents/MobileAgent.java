package agents;

import information.AllInformation;
import information.RawInformation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MobileAgent extends Agent {

    public static final String NAME = "MobileAgent";
    private static final int SCAN = 177;
    private static final int ONE = 179;
    private static final int BACK = 7718;
    private AID stableAgent = new AID(MainAgent.NAME, AID.ISLOCALNAME);
    private Location currentLocation;
    private Location mainContainer;
    private int status = 0;
    private List<AllInformation> allInformation = new ArrayList<>();
    private ServeMovingMessages movingMessagesBehaviour = new ServeMovingMessages();
    private jade.util.leap.List allLocations;
    private int index = 0;
    private String currentConversationId;

    @Override
    protected void setup() {
        currentLocation = here();
        mainContainer = here();
        addBehaviour(movingMessagesBehaviour);
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent done.");
    }

    @Override
    protected void afterMove() {
        System.out.println("Agent moved");
        if (status == ONE) {
            sendBasicInformation();
        } else if (status == BACK) {
            status = ONE;
        } else if (status == SCAN) {
            scanAllAfterMoveProcess();
        }
    }

    private void goBack() {
        status = BACK;
        doMove(mainContainer);
    }

    private void scanAllAfterMoveProcess() {
        AllInformation allInformation = new AllInformation();
        this.allInformation.add(allInformation);
        if (index == allLocations.size() - 1) {
            System.out.println(this.allInformation.toString());
            addBehaviour(movingMessagesBehaviour);
            sendScanAllInformation();
            status = ONE;
        } else {
            doMove((Location) allLocations.get(++index));
        }
    }

    private void sendBasicInformation() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(AllInformation.getInstance().toJson());
        message.addReceiver(stableAgent);
        send(message);
    }

    private void sendScanAllInformation() {
        ACLMessage message = new ACLMessage(ACLMessage.AGREE);
        try {
            message.setContentObject((Serializable) allInformation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setConversationId(currentConversationId);
        message.addReceiver(stableAgent);
        send(message);
        allLocations.clear();
    }

    private void handleMovingRequest(ACLMessage message) {
        try {
            Location newLocation = (Location) message.getContentObject();
            if (newLocation != null && currentLocation != null && newLocation.getAddress().equals(currentLocation.getAddress())) {
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
        currentConversationId = message.getConversationId();
        removeBehaviour(movingMessagesBehaviour);
        status = SCAN;
        allInformation.clear();
        index = 0;
        try {
            allLocations = (jade.util.leap.List) message.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        doMove((Location) allLocations.get(index));
    }

    public class ServeMovingMessages extends CyclicBehaviour {

        private MessageTemplate template = MessageTemplate.or(MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)), MessageTemplate.MatchPerformative(ACLMessage.CFP));

        @Override
        public void action() {
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
                        handleVisitAll(message);
                    case ACLMessage.CANCEL:
                        status = ONE;
                        goBack();
                        break;
                }
            } else block();
        }
    }
}
