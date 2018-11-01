package SupplyStations;

import SupplyStations.Agents.Drivers.AdventurousDriver;
import SupplyStations.Agents.Drivers.CollaborativeDriver;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.SimInit;


public class Launcher extends Repast3Launcher {
    private static final boolean BATCH_MODE = true;
    private static int COLLABORATIVE_DRIVERS = 10;
    private static int ADVENTUROUS_DRIVERS = 10;
    private static boolean DYNAMIC = false;

    private ContainerController mainContainer;

    @Override
    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        mainContainer = rt.createMainContainer(p1);

        launchAgents();
    }


    private void launchAgents() {
        if (DYNAMIC) {
            System.out.println("Launch Dynamic");
        } else {
            launchStaticAgents();
        }
    }

    private void launchStaticAgents(){

        try {
            for (int i = 0; i < ADVENTUROUS_DRIVERS; i++) {
                AdventurousDriver adventurousDriver = new AdventurousDriver();
                mainContainer.acceptNewAgent("AdventurousDriver" + i, adventurousDriver).start();
            }

            // create guided driver agents
            for (int i = 0; i < COLLABORATIVE_DRIVERS; i++) {
                CollaborativeDriver collaborativeDriver = new CollaborativeDriver();
                mainContainer.acceptNewAgent("Collaborative" + i, collaborativeDriver).start();
            }

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "SSS - Supply Station Simulation";
    }

    /**
     * Launching Repast3
     * @param args
     */
    public static void main(String[] args) {
        boolean runMode = !BATCH_MODE;   // BATCH_MODE or !BATCH_MODE

        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        init.loadModel(new Launcher(), null, runMode);
    }
}
