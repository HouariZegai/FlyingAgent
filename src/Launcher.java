import agents.MainAgent;
import agents.MobileAgent;
import controllers.MainController;
import controllers.ScanEachController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    private static final String MAIN_CONTAINER_HOST_IP = "192.168.1.2";
    private static final int MAIN_CONTAINER_PORT = 1091;
    private static final String PLATFORM_ID = "FlyingAgents";
    private static AgentContainer mc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/ScanEach.fxml"));
            Parent root = loader.load();

            ScanEachController homeController = loader.getController();
            initAgent(homeController);

            stage.setScene(new Scene(root));
            stage.setTitle("Flying Agent");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initAgent(ScanEachController controller) {
        // Get a hold on JADE runtime
        Runtime rt = Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        // Launch a complete platform on the 8888 port
        // create a default Profile
        Profile pMain = new ProfileImpl(MAIN_CONTAINER_HOST_IP, MAIN_CONTAINER_PORT, PLATFORM_ID);
        mc = rt.createMainContainer(pMain);

        try {
            AgentController receiverAgent = mc.createNewAgent(MainAgent.NAME, MainAgent.class.getName(), new Object[]{});
            receiverAgent.start();

            AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            //rma.start();

            AgentController mobileAgent = mc.createNewAgent("Service-Agent", MobileAgent.class.getName(), new Object[]{});
            mobileAgent.start();
            controller.setMainAgentController(receiverAgent);
            MainAgent.setHomeController(controller);
        } catch(StaleProxyException spe) {
            spe.printStackTrace();
        }
    }

}