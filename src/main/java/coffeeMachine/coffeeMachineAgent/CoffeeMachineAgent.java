package coffeeMachine.coffeeMachineAgent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
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

        // Av till på vid på command
        fsm.registerTransition(OFF, ON, TURN_ON);
        // Av till av utan command
        fsm.registerTransition(OFF, OFF, TURN_OFF);


        fsm.registerDefaultTransition(ON, HEAT_AND_PRESSURE_READY);

        addBehaviour(fsm);
    }


    private class OffState extends Behaviour {

        private int transition = TURN_OFF;

        @Override
        public void action() {
            printState(this);

            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage requestMessage = myAgent.receive(template);
            if (requestMessage != null) {
                transition = TURN_ON;
                System.out.println("Tog emot en request. Sätter mig till ON!");
            } else {
                block();
            }
        }

        @Override
        public int onEnd() {
            return transition;
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class OnState extends Behaviour {

        @Override
        public void action() {
            printState(this);

            System.out.println("Startar maskin....");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Maskin startad!");
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private class HeatAndPressureReadyState extends Behaviour {

        @Override
        public void action() {
            printState(this);

            
        }

        @Override
        public int onEnd() {
            return super.onEnd();
        }

        @Override
        public boolean done() {
            return true;
        }
    }

    private void printState(Behaviour behaviour) {
        System.out.println("*** Entering state: " + behaviour.getBehaviourName() + " ***");
    }
}