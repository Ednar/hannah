package hannah.hungerAgent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class HungerAgent extends Agent {

    private int fullnessLevel;
    private final String[] acceptableFood = {"Pasta", "Lasagna", "Hamburger"};
    private String cravedFood;

    @Override
    protected void setup() {
        fullnessLevel = 9_001; // Vegeta! What does the....

        addBehaviour(new TickerBehaviour(this, 1_000) {
            @Override
            protected void onTick() {
                if (isHungry()) {
                    cravedFood = getRandomFoodCraving();
                    sendIsHungryMessage();
                    handleFoodProposals();
                } else {
                    decrementFullnessLevel();
                }
            }

            private boolean isHungry() {return fullnessLevel <= 0;}

            private String getRandomFoodCraving() {
                Random random = new Random();
                int randomIndex = random.nextInt(3);
                return acceptableFood[randomIndex];
            }

            private void sendIsHungryMessage() {
                System.out.println("Är hungrig... meddelar hannah");
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setConversationId(ConversationIds.HUNGER);
                message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
                message.setContent("Craving: " + cravedFood);
                message.setSender(myAgent.getAID());
                myAgent.send(message);
                System.out.println("Skickar meddelande om hunger till hannah");
            }

            private void handleFoodProposals() {
                while (isHungry()) {
                    ACLMessage requestMessage = blockingReceive(ACLMessage.PROPOSE);
                    if (requestMessage != null) {
                        ACLMessage response = requestMessage.createReply();
                        if (requestMessage.getContent().equals(cravedFood)) {
                            System.out.println("Tog emot mat med " + requestMessage.getContent() + " enheter");
                            fullnessLevel += 10_000;
                            response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        } else {
                            response.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        }
                        myAgent.send(response);
                    }
                }
            }

            private void decrementFullnessLevel() {
                System.out.println("Hungernivå: " + fullnessLevel);
                fullnessLevel -= 100; // TODO randomisera
            }
        });
    }
}
