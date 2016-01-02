package parent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;

public class ParentAgent extends Agent {

    @Override
    protected void setup() {

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("1) Mata hannah");
                System.out.println("2) Lägg hannah");
                String input = scanner.nextLine();
                int numericInput = Integer.parseInt(input);
                if (numericInput == 1) {
                    feed();
                } else if (numericInput == 2) {
                    putToBed();
                }
            }
        });
    }

    private void feed() {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.HUNGER);
        send(message);
    }

    private void putToBed() {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.SLEEP);
        send(message);
    }
}
