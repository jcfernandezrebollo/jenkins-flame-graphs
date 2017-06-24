package urjc.tfg.flamegraphpipelineplugin;

import java.io.IOException;
import java.util.logging.Logger;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

@Extension
public class Graphic extends RunListener<Run> {
	private static final transient Logger LOGGER = Logger.getLogger(Graphic.class.getName());


    public Graphic() {
        super(Run.class);
    }

	@Override
    public void onCompleted(Run build, TaskListener listener) {
		int number = build.number;
		EnvVars env;
		String name = "";
		String status = "";
		try {
			env = build.getEnvironment(listener);
			name = env.get("JOB_NAME")+"-"+env.get("BUILD_NUMBER");
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		//Se compara el color de la "bola" de la build para ver si mostrar el action o no
		if (build.getBuildStatusUrl().equals("blue.png")){
	    	GraphicAction flameGraph = new GraphicAction(name);
	    	build.getActions().add((Action) flameGraph);
		}

    }

}

