package agents;

// import classes
import behaviours.GetLocationsBehaviour;
import com.google.gson.Gson;
import controllers.DetailPCController;
import controllers.ScanAllController;
import controllers.ScanEachController;
import information.AllInformation;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.List;
import javafx.application.Platform;
import models.Message;

import java.io.IOException;
import java.io.Serializable;


public class MainAgent extends Agent {

    public static final String NAME = "WaiterAgent";
    private static ScanAllController scanAllController;
    private static ScanEachController scanEachController;
    private static DetailPCController detailPCControllerA;

    private Location currentLocation;

    private OneReceiveBehavior oneReceiveBehavior = new OneReceiveBehavior();
    private AgentObjectBehavior agentObjectBehaviour = new AgentObjectBehavior();

    private List availableLocations;

    public static void setScanAllController(ScanAllController scanAllController) {
        MainAgent.scanAllController = scanAllController;
        System.out.println("The scanAllController initilized");
    }

    public static void setScanEachController(ScanEachController scanEachController) {
        MainAgent.scanEachController = scanEachController;
    }

    public static void setDetailController(DetailPCController detailPCController) {
        detailPCControllerA = detailPCController;
    }

    @Override
    protected void setup() {
        setEnabledO2ACommunication(true, 0);
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(MobilityOntology.getInstance());

        addBehaviour(new GetLocationsBehaviour(this));
    }

    private void refreshLocation() {
        removeBehaviour(oneReceiveBehavior);
        removeBehaviour(agentObjectBehaviour);
        addBehaviour(new GetLocationsBehaviour(this));
    }

    private void askForMoving(Location location) {
        System.out.println("The ask For moving was called with location: " + location.toString());
        ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
        message.addReceiver(new AID("Service-Agent", AID.ISLOCALNAME));
        addBehaviour(agentObjectBehaviour);
        addBehaviour(oneReceiveBehavior);
        try {
            message.setContentObject(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(message);
    }

    private void handleO2Object(Message message) {
        switch (message.getRequestType()) {
            case Message.REFRESH_REQUEST:
                refreshLocation();
                break;
            case Message.MOVE_REQUEST:
                currentLocation = (Location) message.getParameters().get(Message.KEY_LOCATION);
                askForMoving(currentLocation);
                break;
            case Message.ASK_REQUEST:
                askMoreInfo();
                break;
            case Message.SCAN_ALL_REQUEST:
                askScanAll();
                break;
        }
    }

    private void askScanAll() {
        addBehaviour(new AskScanAllBehavior());
    }

    private void askMoreInfo() {
        addBehaviour(new AskMoreBehavior());
    }

    public void updateLocations(List items) {
        availableLocations = items;
        System.out.println("Update Location called");
        if (scanEachController != null) {
            Platform.runLater(() -> scanEachController.updateLocation(items));
        } else System.out.println("ScanEach is null");
        addBehaviour(agentObjectBehaviour);
    }

    private class OneReceiveBehavior extends CyclicBehaviour {

        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        @Override
        public void action() {
            ACLMessage message = receive(template);
            if (message != null) {
                AllInformation all = new Gson().fromJson(message.getContent(), AllInformation.class);
                if (scanEachController != null) {
                    Platform.runLater(() -> scanEachController.updateDetail(all));
                }
            } else {
                block();
            }
        }
    }

    private class AgentObjectBehavior extends CyclicBehaviour {
        @Override
        public void action() {
            if (myAgent != null) {
                Object object = myAgent.getO2AObject();
                if (object instanceof Message) {
                    handleO2Object((Message) object);
                } else {
                    block();
                }
            } else {
                block();
            }
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
                    message.addReceiver(new AID("Service-Agent", AID.ISLOCALNAME));
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
            Platform.runLater(() -> detailPCControllerA.updateMoreInfo(rawJson));
            status = DONE;
        }

        @Override
        public boolean done() {
            return status == DONE;
        }
    }

    private class AskScanAllBehavior extends Behaviour {
        private static final int ASK = 0;
        private static final int RESPONSE = 1;
        private static final int DONE = 2;
        private int status = ASK;
        private MessageTemplate template;

        @Override
        public void action() {
            switch (status) {
                case ASK:
                    ACLMessage message = new ACLMessage(ACLMessage.CFP);
                    message.setConversationId(String.valueOf(System.currentTimeMillis()));
                    message.addReceiver(new AID("Service-Agent", AID.ISLOCALNAME));
                    try {
                        message.setContentObject((Serializable) availableLocations);
                        System.out.println("Adding locations successfully.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.AGREE),
                            MessageTemplate.MatchConversationId(message.getConversationId()));
                    send(message);
                    status = RESPONSE;
                    break;
                case RESPONSE:
                    ACLMessage receivedMessage = receive(template);
                    if (receivedMessage != null) {
                        handleScanAll(receivedMessage);
                    } else {
                        block();
                    }
                    break;
            }
        }

        private void handleScanAll(ACLMessage content) {
            System.out.println("Response From Mobile About Scan All");
            scanAllController.updateLocation(availableLocations);
            try {
                @SuppressWarnings("unchecked")
                java.util.List<AllInformation> all = (java.util.List<AllInformation>) content.getContentObject();
                scanAllController.updateInformations(all);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            status = DONE;
        }

        @Override
        public boolean done() {
            return status == DONE;
        }
    }
}
