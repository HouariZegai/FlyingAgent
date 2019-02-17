package agents;

import behaviours.GetLocationsBehaviour;
import com.google.gson.Gson;
import controllers.DetailPCController;
import controllers.HomeController;
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
import jade.util.leap.List;
import javafx.application.Platform;
import models.Message;

import java.io.IOException;
import java.io.Serializable;


public class MainAgent extends Agent {

    public static final String NAME = "WaiterAgent";
    private static HomeController homeControllerA;
    private static DetailPCController detailPCControllerA;
    private Location currentLocation;

    private OneReceiveBehavior oneReceiveBehavior = new OneReceiveBehavior();
    private AgentObjectBehavior myBehaviour = new AgentObjectBehavior();
    private AID mobileAgent = new AID(MobileAgent.NAME, AID.ISLOCALNAME);
    private List availableLocations;

    public static void setHomeController(HomeController homeController) {
        homeControllerA = homeController;
    }

    public static void setDetailController(DetailPCController detailPCController) {
        detailPCControllerA = detailPCController;
        System.out.println("The Controller initilized");
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
        removeBehaviour(myBehaviour);
        addBehaviour(new GetLocationsBehaviour(this));
    }

    private void askForMoving(Location location) {
        ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
        message.addReceiver(new AID("Service-Agent", AID.ISLOCALNAME));
        addBehaviour(myBehaviour);
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
                if (currentLocation == null) {
                    currentLocation = (Location) message.getParameters().get(Message.KEY_LOCATION);
                }
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
        if (homeControllerA != null) {
            Platform.runLater(() -> homeControllerA.updateLocation(items));
        }
        System.out.println("from receiver " + (items));
        addBehaviour(myBehaviour);
    }

    private class OneReceiveBehavior extends CyclicBehaviour {

        MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        @Override
        public void action() {
            ACLMessage message = receive(template);
            if (message != null) {
                System.out.println("From Receiver" + message.getContent());
                AllInformation all = new Gson().fromJson(message.getContent(), AllInformation.class);
                System.out.println("all information is " + all.toString());
                if (homeControllerA != null) {
                    Platform.runLater(() -> homeControllerA.updateDetail(all));
                }
                mobileAgent = message.getSender();
            } else {
                block();
            }
        }

        private void handleMessage(String jsonResponse) {
            System.out.println(jsonResponse);
        }
    }

    private class AgentObjectBehavior extends CyclicBehaviour {
        @Override
        public void action() {
            Object object = myAgent.getO2AObject();
            if (object instanceof Message) {
                handleO2Object((Message) object);
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
                    message.addReceiver(mobileAgent);
                    try {
                        message.setContentObject((Serializable) availableLocations);
                        System.out.println("Adding locations successfully.");
                    } catch (IOException e) {
                        System.out.println("Exception in Sending data.");
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
                        handleScanAll(receivedMessage.getContent());
                    } else {
                        block();
                    }
                    break;
            }
        }

        private void handleScanAll(String content) {

        }

        @Override
        public boolean done() {
            return status == DONE;
        }
    }
}
