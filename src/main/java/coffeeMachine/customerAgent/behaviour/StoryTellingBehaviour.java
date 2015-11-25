package coffeeMachine.customerAgent.behaviour;

import jade.core.behaviours.Behaviour;

import java.util.Random;
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
                        "*       Navigate the office: go [direction]             *\n" +
                        "*       Fighting the monster: press [button]            *\n" +
                        "*********************************************************\n");

                slowWrite("It's time.");
                dramaticPause();
                fastWrite("In the distance you can barely make out the shape of a curious contraption...");
                dramaticPause();
                slowWrite("The coffee machine whispers to you, beckoning for you.");
                System.out.print("What do you do? ");
                choice = scanner.nextLine();

                if (choiceIs("go north")) {
                    System.out.println();
                } else if (choiceContainsDirection(choice)) {
                    System.out.println();
                    dramaticPause();
                    slowWrite("You are now in a forrest");
                    while (true) {
                        System.out.print("What do you do? ");
                        choice = scanner.nextLine();
                        Random random = new Random();
                        int randomValue = random.nextInt(27) + 97;
                        char randomLetter = (char) randomValue;
                        if (choice.contains(randomLetter + "")) {
                            slowWrite("You stumble and fall, breaking your ankles. \n" +
                                    "In a clearing ahead you see movement - hope, a rescuer? \n");
                            dramaticPause();
                            fastWrite("No!");
                            dramaticPause();
                            slowWrite("Wild beast kill you and feed your remains to their young.\nYou have failed the quest.");
                            quit();
                        }
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

                if (choiceContains(choice, "on")) {
                    storyBoard.step = StoryBoardBehaviour.Step.TURN_ON;
                    chapter = 3;
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

                if (choiceContains(choice, "espresso")) {
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

                if (choiceContains(choice, "steam")) {
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

    private boolean choiceContainsDirection(final String choice) {return this.choice.trim().contains("go") || this.choice.trim().contains("GO");}

    private boolean choiceContains(String choice, String keyword)
    {
        String processedChoice = choice.trim().toLowerCase();
        String processedKeyword = keyword.trim().toLowerCase();
        return processedChoice.contains(processedKeyword);
    }

    private void quit() {System.exit(0);}
}
