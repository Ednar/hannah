package hannah.sleepAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SleepAgent extends Agent {

    private int energy;

    @Override
    protected void setup() {
        energy = 30_000;
        addBehaviour(new TickerBehaviour(this, 5_000) {
            @Override
            protected void onTick() {
                if (energy <= 0) {
                    // Gråt
                    System.out.println("Är trött... meddelar hannah");
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                    message.setConversationId(ConversationIds.SLEEP);
                    message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
                    message.setSender(myAgent.getAID());
                    myAgent.send(message);
                    System.out.println("Skickar meddelande till hannah om att det är dags att sova");

                    ACLMessage requestMessage = blockingReceive();
                    if(requestMessage.getConversationId().equals(ConversationIds.SLEEP)) {
                        System.out.println("Sover lite...");
                        energy = 300_000; // ska inte bli trött igen under demo
                    }

                } else {
                    // konsumera energi
                    System.out.println("Sömnivå: " + energy);
                    energy -= 1000; // TODO randomisera
                }
            }
        });
    }
}