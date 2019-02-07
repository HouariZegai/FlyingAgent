package agents;

import behaviours.GetAvailableLocationsBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.mobility.MobilityOntology;

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
}
