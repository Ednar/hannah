package hannah.behaviour;

import hannah.ConcurrentMoodHannah;
import hannah.utils.ConversationIds;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserInteractionBehaviour extends Behaviour {

    private final ConcurrentMoodHannah hannah;

    public UserInteractionBehaviour(final ConcurrentMoodHannah hannah) {
        this.hannah = hannah;
    }

    @Override
    public void action() {
        MessageTemplate interactionTemplate =
                MessageTemplate.MatchSender(new AID("parent", AID.ISLOCALNAME));
        ACLMessage message = hannah.receive(interactionTemplate);
        if (message != null) { // Om någon användsinput finns
            String conversationId = message.getConversationId();
            switch (conversationId) {
                case ConversationIds.HUNGER:
                    hannah.feed(message.getContent());
                    break;
                case ConversationIds.SLEEP:
                    hannah.rest();
            }
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
