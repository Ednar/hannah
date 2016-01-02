package parent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;

public class ParentAgent extends Agent {

    private Scanner scanner = new Scanner(System.in);

    @Override
    protected void setup() {

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
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
        System.out.println("Select food option");
        System.out.println("1) Pasta");
        System.out.println("2) Lasagna");
        System.out.println("3) Hamburger");
        int input = Integer.parseInt(scanner.nextLine());
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.HUNGER);
        switch (input){
            case 1: message.setContent("Pasta");
                break;
            case 2: message.setContent("Lasagna");
                break;
            case 3: message.setContent("Hamburger");
                break;
        }
        send(message);
    }

    private void putToBed() {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.SLEEP);
        send(message);
    }
}
