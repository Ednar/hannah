package parent;

import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;

public class ParentAgent extends Agent {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    protected void setup() {

        ParentGui gui = new ParentGui(this);
    }

    public void wake() {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.WAKE);
        send(message);
    }

    public void feed(int dish) {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.HUNGER);
        switch (dish){
            case 1: message.setContent("Pasta");
                break;
            case 2: message.setContent("Lasagna");
                break;
            case 3: message.setContent("Hamburger");
                break;
        }
        send(message);
    }

    public void putToBed() {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID("hannah", AID.ISLOCALNAME));
        message.setConversationId(ConversationIds.SLEEP);
        send(message);
    }
}
