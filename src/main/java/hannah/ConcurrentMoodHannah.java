package hannah;

import hannah.behaviour.UserInteractionBehaviour;
import hannah.utils.ConversationIds;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Hannah är en komplicerad varelse som kan känna av sin omgivning och reagera
 * utifrån sina percept.
 *
 * Ett cykliskt beteende kontrollerar ständigt hannahs välmående. Genom ett
 * stegvis beteende (MoodStepBehaviour) kontrolleras alla percept. Eftersom
 * enbart ett beteende kan köra åt gången kommer det cykliska beteende beteende
 * att stämma av humöret efter varje avslutat steg i moodstep.
 *
 * Ytterliga ett beteende kontrollerar om någon input finns att hantera. Input
 * sköts via en extern agent (InterractionAgent).
 */
public class ConcurrentMoodHannah extends Agent {

    private boolean hungry;
    private boolean sleepy;
    private boolean warm;
    private SensesManager sensesManager;

    @Override
    protected void setup() {

        registerSelfWithDF();

        sensesManager = new SensesManager(this);
        sensesManager.addHungerAgent();
        sensesManager.addSleepAgent();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (happy()) {
                    System.out.println("Hannah är glad (skratt)");
                } else {
                    System.out.println("Hannah är lessen... :( (gråt)");
                }
                // Vänta lite innan nästa behov kontrolleras. Egentligen bara för
                // Ljud ska hinna spelas
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Hanterar kontroll av känslor
        addBehaviour(new MoodStepBehaviour());
        // Hanterar kontroll av input
        addBehaviour(new UserInteractionBehaviour(this));
    }

    //TODO implementera DF i hela programmet eller ta bort det
    private void registerSelfWithDF() {DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }



    private boolean happy() {
        return !hungry && !sleepy && !warm;
    }

    private class MoodStepBehaviour extends Behaviour {

        private int step = 0;

        @Override
        public void action() {

            switch (step) {
                // Kollar hunger
                case 0:
                    System.out.println("Kontrollerar hunger...");
                    // Se om det finns något inform-meddelande som har ämnet hunger.
                    // Ta bara emot ett sådant meddelande
                    MessageTemplate hungerTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(ConversationIds.HUNGER));

                    // Ta emot ett meddelaned. Om det är null fanns ingen hunger.
                    ACLMessage hungerResponse = receive(hungerTemplate);
                    if (hungerResponse != null) {
                        hungry = true;
                    }
                    step++; // kolla nästa humör
                    break;
                // Kolla sömnig
                case 1:
                    System.out.println("Kontrollerar sömn...");
                    // Se om det finns något inform-meddelande som har ämnet sleepy. Ta bara emot ett sådant meddelande
                    MessageTemplate sleepyTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(ConversationIds.SLEEP));

                    // Ta emot ett meddelande. Om det är null fanns ingen sömn.
                    ACLMessage sleepyMessage = receive(sleepyTemplate);
                    if (sleepyMessage != null) {
                        sleepy = true;
                    }
                    step++; // Kolla nästa humör
                    break;
            }
        }

        @Override
        public boolean done() {
            if (step > 1) {
                step = 0; // Börja om
            }
            return false;
        }
    }

    public void feed(final int calories) {
        if (hungry) {
            ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
            message.addReceiver(sensesManager.getHungerAgentAID());
            message.setConversationId(ConversationIds.HUNGER);
            message.setContent(String.valueOf(calories));
            send(message);
            hungry = false; // TODO här bör hungeragenten bedöma om hunger finns
        }

    }

    public void rest() {
        if (sleepy) {
            ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
            message.addReceiver(sensesManager.getSleepAgentAID());
            message.setConversationId(ConversationIds.SLEEP);
            send(message);
            sleepy = false; // TODO här bör sömnagenten bedöma om hunger finns
            System.out.println("Hannah sover 15 sekunder... reagerar inte på input");
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
