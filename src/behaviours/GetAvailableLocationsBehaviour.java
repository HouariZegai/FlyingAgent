package behaviours;

import agents.MobileAgent;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Location;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;


public class GetAvailableLocationsBehaviour extends SimpleAchieveREInitiator {

    public GetAvailableLocationsBehaviour(MobileAgent a) {
        // call the constructor of FipaRequestInitiatorBehaviour
        super(a, new ACLMessage(ACLMessage.REQUEST));
        ACLMessage request = (ACLMessage) getDataStore().get(REQUEST_KEY);
        // fills all parameters of the request ACLMessage
        request.clearAllReceiver();
        request.addReceiver(a.getAMS());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        request.setOntology(MobilityOntology.NAME);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        // creates the content of the ACLMessage
        try {
            Action action = new Action();
            action.setActor(a.getAMS());
            action.setAction(new QueryPlatformLocationsAction());
            a.getContentManager().fillContent(request, action);
        } catch (Exception fe) {
            fe.printStackTrace();
        }
        reset(request);
    }

    protected void handleNotUnderstood(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleNotUnderstood : " + reply.toString());
    }

    protected void handleRefuse(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleRefuse : " + reply.toString());
    }

    protected void handleFailure(ACLMessage reply) {
        System.out.println(myAgent.getLocalName() + " handleFailure : " + reply.toString());
    }

    protected void handleAgree(ACLMessage reply) {
    }

    protected void handleInform(ACLMessage inform) {
        String content = inform.getContent();
        System.out.println("Content is:" + content);
        try {
            Result results = (Result) myAgent.getContentManager().extractContent(inform);
            System.out.println((results.getItems().toString()));

            myAgent.doMove((Location) results.getItems().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
