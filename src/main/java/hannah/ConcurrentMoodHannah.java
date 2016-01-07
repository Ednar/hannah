package hannah;

import hannah.behaviour.UserInteractionBehaviour;
import hannah.utils.AudioPlayer;
import hannah.utils.ConversationIds;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;
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
    private boolean cold;

    private SensesManager sensesManager;
    private final AudioPlayer player  = new AudioPlayer();
    private boolean sleeping = false;

    @Override
    protected void setup() {

        registerSelfWithDF();

        sensesManager = new SensesManager(this);
        sensesManager.addHungerAgent();
        sensesManager.addSleepAgent();
        sensesManager.addTemperatureAgent();

        // Hanterar kontroll av känslor
        addBehaviour(new MoodStepBehaviour());
        // Hanterar kontroll av input
        addBehaviour(new UserInteractionBehaviour(this));

        // Hanterar sinnen
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (sleeping) {
                    // I demo är det bara förälder som väcker hannah
                    player.play("snore.wav");
                    try {
                        TimeUnit.SECONDS.sleep(4);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else if (happy()) {
                    // Spelar ett slumpmässigt glädjeljud
                    Random random = new Random();
                    int randomGlad = random.nextInt(20);
                    if (randomGlad < 1 ) {
                        player.play("giggle.wav");
                        try {
                            TimeUnit.SECONDS.sleep(8); // kör detta för det är så cute
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.play("talk.wav");
                    }

                } else {
                    player.play("cry.wav");
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
        return !hungry && !sleepy && !warm && !cold;
    }

    private class MoodStepBehaviour extends Behaviour {

        private int step = 0;

        @Override
        public void action() {

            switch (step) {
                // Kollar hunger
                case 0:
                    // Se om det finns något inform-meddelande som har ämnet hunger.
                    // Ta bara emot ett sådant meddelande
                    MessageTemplate hungerTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(ConversationIds.HUNGER));

                    // Ta emot ett meddelaned. Om det är null fanns ingen hunger.
                    ACLMessage hungerResponse = receive(hungerTemplate);
                    if (hungerResponse != null) {
                        hungry = true;
                        System.out.println("hannah vill ha: " + hungerResponse.getContent());
                    }
                    step++; // kolla nästa humör
                    break;
                // Kolla sömnig
                case 1:
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
                case 2:
                    ACLMessage temperatureRequest = new ACLMessage(ACLMessage.REQUEST);
                    temperatureRequest.addReceiver(sensesManager.getTemperatureAID());
                    temperatureRequest.setConversationId(ConversationIds.TEMP);
                    send(temperatureRequest);

                    MessageTemplate temperatureTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(ConversationIds.TEMP));

                    // Ta emot ett meddelande. Om det är null fanns ingen sömn.
                    ACLMessage temperatureMessage = receive(temperatureTemplate);
                    if (temperatureMessage != null) {
                        double temperature = Double.parseDouble(temperatureMessage.getContent());
                        System.out.println("Kroppstemperatur: " + temperature);
                        if (temperature > 40) {
                            killHannah();
                        } else if (temperature > 25) {
                            warm = true;
                        } else  if (temperature < 20) {
                            cold = true;
                        } else {
                            warm = false;
                            cold = false;
                        }
                    }
                    step++; // Kolla nästa humör
                    break;

            }
        }

        @Override
        public boolean done() {
            if (step > 2) {
                step = 0; // Börja om
            }
            return false;
        }
    }

    private void killHannah() {
        player.play("death.wav");
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doDelete();
    }

    public void feed(final String dish) {
        if (hungry) {
            ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
            message.addReceiver(sensesManager.getHungerAgentAID());
            message.setConversationId(ConversationIds.HUNGER);
            message.setContent(String.valueOf(dish));
            send(message);

            ACLMessage response = blockingReceive(MessageTemplate.MatchConversationId(ConversationIds.HUNGER));
            if (response.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                hungry = false;
                player.play("eating.wav");
                try {
                    TimeUnit.SECONDS.sleep(45); // Tid det tar att äta
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Hannah ville inte ha " + dish);
                player.play("nejtack.wav");
                try {
                    TimeUnit.SECONDS.sleep(3); // Tid det tar att skrika
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void rest() {
        if (sleepy && !hungry && !cold && !warm) {
            ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
            message.addReceiver(sensesManager.getSleepAgentAID());
            message.setConversationId(ConversationIds.SLEEP);
            send(message);
            sleepy = false; // TODO här bör sömnagenten bedöma om hunger finns
            System.out.println("Hannah sover... reagerar inte på input");
            sleeping = true;
        }
    }

    // Metod för demo
    public void abortSleep() {
        sleeping = false;
    }

}
