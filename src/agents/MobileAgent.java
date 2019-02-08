package agents;

import behaviours.GetAvailableLocationsBehaviour;
import information.AllInformation;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;

public class MobileAgent extends Agent {

    @Override
    protected void setup() {
        // register the SL0 content language
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        // register the mobility ontology
        getContentManager().registerOntology(MobilityOntology.getInstance());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addBehaviour(new GetAvailableLocationsBehaviour(this));
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent done.");
    }

    @Override
    protected void afterMove() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(AllInformation.getInstance().toJson());
        message.addReceiver(new AID("Waiter", AID.ISLOCALNAME));
        send(message);
    }
}
