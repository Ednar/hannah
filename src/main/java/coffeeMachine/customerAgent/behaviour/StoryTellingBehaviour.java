package coffeeMachine.customerAgent.behaviour;

import jade.core.behaviours.Behaviour;

import java.util.Scanner;

import static coffeeMachine.story.StoryTellingUtils.*;

public class StoryTellingBehaviour extends Behaviour {

    private Scanner scanner = new Scanner(System.in);
    private StoryBoardBehaviour storyBoard;
    private String choice;

    private int chapter = 1;


    public StoryTellingBehaviour(final StoryBoardBehaviour storyBoard) {
        this.storyBoard = storyBoard;
    }

    @Override
    public void action() {
        switch (chapter) {
            case 1:
                turboWrite("\n" +
                        "\n" +
                        " _____        __  __            _____                 _   \n" +
                        "/  __ \\      / _|/ _|          |  _  |               | |  \n" +
                        "| /  \\/ ___ | |_| |_ ___  ___  | | | |_   _  ___  ___| |_ \n" +
                        "| |    / _ \\|  _|  _/ _ \\/ _ \\ | | | | | | |/ _ \\/ __| __|\n" +
                        "| \\__/\\ (_) | | | ||  __/  __/ \\ \\/' / |_| |  __/\\__ \\ |_ \n" +
                        " \\____/\\___/|_| |_| \\___|\\___|  \\_/\\_\\\\__,_|\\___||___/\\__|\n" +
                        "                                                          \n" +
                        "*********************************************************\n" +
                        "\n");
                System.out.println("It's time.");
                dramaticPause();
                fastWrite("In the distance you can see it...");
                dramaticPause();
                slowWrite("The coffee machine whispers to you, calling you.");
                System.out.print("What do you do? ");
                choice = scanner.nextLine();

                if (choiceIs("go north")) {
                    System.out.println();
                } else if (choiseContainsGo(choice)) {
                    System.out.println();
                    dramaticPause();
                    slowWrite("You enter in a forrest");
                    while (true) {
                        System.out.print("What do you do? ");
                        choice = scanner.nextLine();
                        slowWrite("You go deeper into the forrest");
                    }
                } else {
                    slowWrite("You are forever lost, wandering the catacombs of the office space.");
                    quit();
                }
                chapter = 2;
                break;
            case 2:
                dramaticPause();
                System.out.println("");
                slowWrite("You move forward...");
                System.out.println();
                fastWrite("Towering ahead of you, you see it.");
                dramaticPause();
                slowWrite("The coffee machine...");
                dramaticPause();
                System.out.println();

                fastWrite("You steel yourself for the inevitable battle.");
                dramaticPause();
                slowWrite("Patiently you await its first move.");
                System.out.println();
                dramaticPause();
                dramaticPause();
                slowWrite("Nothing is happening...");
                dramaticPause();

                slowWrite("The beast slumbers...");
                dramaticPause();
                System.out.println();

                slowWrite("Inspecting the apparatus you notice three switches, aptly labeled: ");
                fastWrite("\tON\tESPRESSO\tSTEAM");
                dramaticPause();
                slowWrite("Can you 'press' them? Should you?");
                System.out.print("What do you do? ");
                choice = scanner.nextLine();

                if (choiceIs("press on")) {
                    //      coffeeMakingHandler.sendSilent(ACLMessage.REQUEST);
                    storyBoard.step = StoryBoardBehaviour.Step.TURN_ON;
                    chapter = 3;

                    //      coffeeMakingHandler.receiveSilent(ACLMessage.AGREE, StoryBoard.Step.WAIT_FOR_DAVE_TO_HEAT_AND_PRESSURIZE);

                } else {
                    slowWrite("Butter fingers! You fumble your hand into a wall-socket and die. Life can be like that.");
                    quit();
                }
                break;
            case 3:
                dramaticPause();
                System.out.println();
                slowWrite("............");
                slowWrite("wrrrmmmmmmmm......");
                fastWrite("The hums and rattles startle you!");
                dramaticPause();
                System.out.println();

                fastWrite("The battle begins!");
                System.out.println();

                dramaticPause();
                slowWrite("Lights flicker before they settle to a fierce glow.");
                System.out.println();
                dramaticPause();

                slowWrite("Nothing is happening...");
                dramaticPause();
                slowWrite("In your mind you can still hear it.");
                epicSlowWrite("Beckoning...");
                dramaticPause();
                System.out.println();

                slowWrite("Still, all appearing before you are the same switches, now faintly glowing");
                fastWrite("\tON/OFF\tESPRESSO\tSTEAM");
                dramaticPause();
                System.out.print("What do you do? ");
                choice = scanner.nextLine();

                if (choiceIs("press espresso")) {
                    //        coffeeMakingHandler.receiveSilent(ACLMessage.REQUEST, StoryBoard.Step.TURNING_ESPRESSO_KNOB);
                    //         coffeeMakingHandler.sendSilent(ACLMessage.AGREE);
                    storyBoard.step = StoryBoardBehaviour.Step.TURNING_ESPRESSO_KNOB;
                    chapter = 4;

                } else {
                    fastWrite("As if out of nowhere the machine lunges into action! A legendary battle ensues. " +
                            "Alas, you die. The age of machines begins.");
                    quit();
                }
                dramaticPause();
                System.out.println();
                break;
            case 4:
                slowWrite("What sorcery is this?");
                dramaticPause();
                fastWrite("You watch as a black liquid slowly pours into a cup in the maw of the machine.");
                dramaticPause();
                slowWrite("A poison attack? In a fit of rage, you panic to find  an antidote.");
                dramaticPause();
                System.out.print("What do you do? ");
                choice = scanner.nextLine();

                if (choiceIs("press steam")) {
                    storyBoard.step = StoryBoardBehaviour.Step.SKIMMING_MILK;
                    chapter = 5;
                } else {
                    slowWrite("Blinded by rage, you suffer a heart attack and die. This is why your doctor told you lay off caffeine." +
                            "If only you would have listened.... if only....");
                    quit();
                }
                break;
            case 5:
                dramaticPause();
                slowWrite("You watch as steaming, white liquid pours into the cup.");
                dramaticPause();
                fastWrite("Can you drink this?");
                dramaticPause();
                slowWrite("You gently take a sip from the cup.");
                slowWrite("And another.");
                dramaticPause();
                slowWrite("And another....");
                System.out.println();

                slowWrite("Coming to your senses, you realize that you've been away from your desk for too long. \n" +
                        "Your boss is staring you down from across the hallway while you finish your coffee.\n" +
                        "The quest is over, but who really won? ");
                dramaticPause();
                System.out.println();
                fastWrite("34 years later you die of old age. In hindsight, the coffee quest was pointless.");
                storyBoard.step = StoryBoardBehaviour.Step.DONE;
                break;
            default:
                throw new NullPointerException();
        }


    }

    @Override
    public boolean done() {
        return false;
    }

    private boolean choiceIs(final String choice) {return this.choice.trim().equalsIgnoreCase(choice);}

    private boolean choiseContainsGo(final String choice) {return this.choice.trim().contains("go") || this.choice.trim().contains("GO");}

    private void quit() {System.exit(0);}
}
