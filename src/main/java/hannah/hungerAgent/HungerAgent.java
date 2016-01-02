package hannah.hungerAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class HungerAgent extends Agent {

    private int fullnessLevel;

    @Override
    protected void setup() {
        fullnessLevel = 9_001; // Vegeta! What does the....

        addBehaviour(new TickerBehaviour(this, 5_000) {
            @Override
            protected void onTick() {
                if (fullnessLevel <= 0) {
                    // Gråt
                    System.out.println("Är hungrig... meddelar hannah");
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                    message.setConversationId(ConversationIds.HUNGER);
                    message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
                    message.setSender(myAgent.getAID());
                    myAgent.send(message);
                    System.out.println("Skickar meddelande om hunger till hannah");

                    ACLMessage requestMessage = blockingReceive();

                    if(requestMessage.getConversationId().equals(ConversationIds.HUNGER)) {
                        System.out.println("Tog emot mat med " + requestMessage.getContent() + " enheter");
                        fullnessLevel += Integer.parseInt(requestMessage.getContent());
                    }


                } else {
                    // konsumera energi
                    System.out.println("Hungernivå: " + fullnessLevel);
                    fullnessLevel -= 1000; // TODO randomisera
                }
            }
        });
    }
}
