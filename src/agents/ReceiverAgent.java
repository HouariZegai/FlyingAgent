package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceiverAgent extends Agent {

    public static final String NAME = "WaiterAgent";

    @Override
    protected void setup() {
        addBehaviour(new ReceivingBehavior());
    }

    class ReceivingBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                String content = message.getContent();
                System.out.println("The message is " + content);
            } else {
                block();
            }
        }
    }
}
