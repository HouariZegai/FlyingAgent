package agents;

import controllers.ScanAllController;
import controllers.ScanEachController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXMLLoader;
import utils.Constants;

public class AgentsContainerInstance {

    private static AgentController mainController;
    private static AgentsContainerInstance ourInstance;

    public static AgentController getInstance(FXMLLoader scanEachLoader, FXMLLoader scanAllLoader) {
        if (ourInstance == null) {
            ourInstance = new AgentsContainerInstance(scanEachLoader, scanAllLoader);
        } else {
            ScanEachController scanEachController = scanEachLoader.getController();
            scanEachController.setMainAgentController(mainController);
            MainAgent.setScanEachController(scanEachController);

            ScanAllController scanAllController = scanAllLoader.getController();
            scanAllController.setMainAgentController(mainController);
            MainAgent.setScanAllController(scanAllController);
        }
        return mainController;
    }

    private AgentsContainerInstance(FXMLLoader scanEachLoader, FXMLLoader scanAllLoader) {
        Runtime rt = Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        // Launch a complete platform on the 8888 port
        // create a default Profile
        Profile pMain = new ProfileImpl(Constants.MAIN_CONTAINER_HOST_IP, Constants.MAIN_CONTAINER_PORT, Constants.PLATFORM_ID);
        AgentContainer mc = rt.createMainContainer(pMain);

        try {
            AgentController receiverAgent = mc.createNewAgent(MainAgent.NAME, MainAgent.class.getName(), new Object[]{});
            receiverAgent.start();
            mainController = receiverAgent;
            AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            //rma.start();
            AgentController mobileAgent = mc.createNewAgent("Service-Agent", MobileAgent.class.getName(), new Object[]{});
            mobileAgent.start();

            ScanEachController scanEachController = scanEachLoader.getController();
            scanEachController.setMainAgentController(receiverAgent);
            MainAgent.setScanEachController(scanEachController);

            ScanAllController scanAllController = scanAllLoader.getController();
            scanAllController.setMainAgentController(receiverAgent);
            MainAgent.setScanAllController(scanAllController);

        } catch (StaleProxyException spe) {
            spe.printStackTrace();
        }
    }
}
