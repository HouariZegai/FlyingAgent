import jade.domain.FIPAException;
import javafx.application.Application;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import agents.MobileAgent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    static AgentContainer mc;
    Runtime rt;

    public Launcher() {

        try {
            // Get a hold on JADE runtime
            rt = Runtime.instance();

            // Exit the JVM when there are no more containers around
            rt.setCloseVM(true);

            // Launch a complete platform on the 8888 port
            // create a default Profile
            Profile pMain = new ProfileImpl(null, 8888, null);

            System.out.println("Launching a whole in-process platform..." + pMain);
            mc = rt.createMainContainer(pMain);

        /* set now the default Profile to start a container
        ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
        System.out.println("Launching the agent container ..."+pContainer);
        AgentContainer cont = rt.createAgentContainer(pContainer);
        System.out.println("Launching the agent container after ..."+pContainer);*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            AgentController mobileAgent = mc.createNewAgent("Service-Agent", MobileAgent.class.getName(), new Object[]{});
            mobileAgent.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/DetailPC.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        stage.setTitle("Flying Agent");
        stage.show();
        stage.setOnCloseRequest(e -> {
            try {
                mc.kill();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}