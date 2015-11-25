package coffeeMachine.coffeeMachineAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.concurrent.TimeUnit;

public class CoffeeMachineAgent extends Agent {

    // States
    private static final String OFF = "OFF";
    private static final String ON = "ON";

    // Commands
    private static final int TURN_ON = 1;
    private static final int TURN_OFF = 0;
    private static final String HEAT_AND_PRESSURE_READY = "HEAT AND PRESSURE READY";
    private static final String STEAMER_READY = "STEAMER READY";
    private MessageTemplate template;
    private AID coffeeBuyer;

    @Override
    protected void setup() {
        FSMBehaviour fsm = new FSMBehaviour(this) {
            @Override
            public int onEnd() {
                System.out.println("Coffee sold!");
                myAgent.doDelete();
                return super.onEnd();
            }
        };

        fsm.registerFirstState(new OffState(), OFF);
        fsm.registerState(new OnState(), ON);
        fsm.registerState(new HeatAndPressureReadyState(), HEAT_AND_PRESSURE_READY);
        fsm.registerState(new SteamerReadyState(), STEAMER_READY);

        // Av till på vid på command
        fsm.registerTransition(OFF, ON, TURN_ON);
        // Av till av utan command
        fsm.registerTransition(OFF, OFF, TURN_OFF);

        // Går automatiskt från ON till att värme och tryck är klart
        fsm.registerDefaultTransition(ON, HEAT_AND_PRESSURE_READY);
        // Efter att espresso är bryggt går den automatiskt till att mjölk-saken är redo
        fsm.registerDefaultTransition(HEAT_AND_PRESSURE_READY, STEAMER_READY);

        fsm.registerDefaultTransition(STEAMER_READY, OFF);

        addBehaviour(fsm);
    }

    private class OffState extends OneShotBehaviour {

        @Override
        public void action() {
            printState(this);
            receiveMessage(ACLMessage.REQUEST, "Tog emot request. Sätter mig till on");
            sendMessage(ACLMessage.AGREE);
            pauseFor(1);
            System.out.println("Maskin sätter sig till ON");
        }

        @Override
        public int onEnd() {
            return TURN_ON;
        }
    }

    private class OnState extends OneShotBehaviour {

        @Override
        public void action() {
            printState(this);

            System.out.println("Startar maskin....");
            pauseFor(2);
            System.out.println("Maskin startad!");

            sendMessage(ACLMessage.REQUEST);
            System.out.println("*** SKICKAR request till " + coffeeBuyer.getLocalName() + "... dags att göra espresso!");
        }

    }

    private class HeatAndPressureReadyState extends OneShotBehaviour {

        @Override
        public void action() {
            printState(this);

            receiveMessage(ACLMessage.AGREE, "Tog emot en agree. Dags att göra kaffe!");
        }
    }


    private class SteamerReadyState extends OneShotBehaviour {

        @Override
        public void action() {
            System.out.println("Heating steamer....");
            pauseFor(2);
            System.out.println("Steamer ready");


            sendMessage(ACLMessage.REQUEST);
            System.out.println("*** skickar request till " + coffeeBuyer.getLocalName() + "... dags att hetta upp mjölk!");

            pauseFor(1);
            receiveMessage(ACLMessage.AGREE, "Tog emot en agree. Kaffe latte är klar! Stänger maskin");
            pauseFor(1);

            sendMessage(ACLMessage.REQUEST); // Du kan ta din kaffe nu, kompis.
        }
    }

    @SuppressWarnings("Duplicates")
    private void sendMessage(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(coffeeBuyer);
        message.setSender(getAID());
        send(message);
        System.out.println("Skickar meddelande till " + coffeeBuyer.getLocalName() + " " + performative);
    }

    private void receiveMessage(int performative, String message) {
        ACLMessage requestMessage = blockingReceive();
        coffeeBuyer = requestMessage.getSender();
        if (requestMessage.getPerformative() == performative) {
            System.out.println("Tar emot meddelande från kaffeköpare: " + requestMessage.getPerformative());
            System.out.println(message);
        }
    }

    private void pauseFor(final int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printState(Behaviour behaviour) {
        System.out.println("*** Entering state: " + behaviour.getBehaviourName() + " ***");
    }
}