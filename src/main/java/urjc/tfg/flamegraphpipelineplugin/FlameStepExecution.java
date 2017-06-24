package urjc.tfg.flamegraphpipelineplugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jenkinsci.plugins.workflow.steps.BodyExecution;
import org.jenkinsci.plugins.workflow.steps.BodyExecutionCallback;
import org.jenkinsci.plugins.workflow.steps.BodyInvoker;
import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Build;
import hudson.model.Computer;
//import hudson.model.Run;
//import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.CauseOfInterruption;

@SuppressFBWarnings("SE_INNER_CLASS")
public class FlameStepExecution extends SynchronousNonBlockingStepExecution<Boolean>{
	private static final long serialVersionUID = 1L;

	private static final transient Logger LOGGER = Logger.getLogger(FlameStepExecution.class.getName());

	
	//no se usa pero en un futuro lo mismo si
	@StepContextParameter
	private transient FilePath workspace;
	@StepContextParameter
	private transient EnvVars env;
	@StepContextParameter
	private transient TaskListener listener;
	@StepContextParameter
	private transient Computer computer;
	
    @SuppressFBWarnings(value="SE_TRANSIENT_FIELD_NOT_RESTORED", justification="Only used when starting.")
    private transient FlameStep step;
    private BodyExecution body;	
	private int existFlame;
	public String flameName;
	
	FlameStepExecution(FlameStep step, StepContext context) throws Exception{
		super(context);
		this.step = step;
		env = context.get(EnvVars.class);
		
	}
	
	@Override
	protected Boolean run() throws Exception {
		StepContext context = getContext();
		LOGGER.info("START CREATING FLAMEGRAPH");
		this.flameName = this.jobName()+"-"+this.buildNumber();
		existFlame = generateFlameGraph();
//		body = this.getContext().newBodyInvoker().withDisplayName(flameName).withCallback(BodyExecutionCallback.wrap(getContext())).start();
		//getContext().newBodyInvoker().withDisplayName("Jenkins Flame Graph").withCallback(BodyExecutionCallback.wrap(getContext())).start();
		if (existFlame == 0){
			LOGGER.info("FLAMEGRAPH CREATED NAMED "+flameName);
			return true;

		}
		else{
			LOGGER.info("FLAMEGRAPH ERROR (CHECK OUTPUT FILES \"outNormal.txt\" AND \"outError.txt\" AT work/jobs/"+this.jobName()+"/builds/"+this.buildNumber());
			throw new Exception("ERROR GENERATING FLAMEGRAPH");
		}
	}
	
	private int generateFlameGraph(){
		String path = "./src/main/resources/scripts/";
		int seconds = this.step.getSeconds();
		String sleepRecord = String.valueOf(seconds);
		String sleepScript = String.valueOf(seconds+5);
		int exit = -1;
        try {
//        	./scritp "argumentos" "jobName" "buildNumber" "segundos para perfRecord" "segundos para perfScript" "flameType"?
    		ProcessBuilder builder = new ProcessBuilder("sh",path+"script.sh",this.step.getArgs(),this.jobName(),this.buildNumber(),sleepRecord,sleepScript,this.step.getFlameType());
    		builder.redirectOutput(new File("outNormal.txt"));
    		builder.redirectError(new File("outError.txt"));
    		Process p = builder.start();
    		p.waitFor();
    		exit = p.exitValue();
    		return exit;
        } catch (Exception e) {
        	return exit;
        }
    }
	
	public String jobName(){
		return env.get("JOB_NAME");
	}
	
	public String buildNumber(){
		return env.get("BUILD_NUMBER");
	}
	

	
	@Override
	public void onResume(){
		//generateFlameGraph();
	}
	
	//no se usa pero en un futuro lo mismo si
	private TaskListener listener(){
		try{
			return getContext().get(TaskListener.class);
		} catch (Exception x){
			LOGGER.log(Level.WARNING, null, x);
			return TaskListener.NULL;
		}
	}
	
	@Override
	public void stop(Throwable cause) throws Exception {
		if (body != null){
			body.cancel(cause);
		}
		
	}
	
	private class Callback extends BodyExecutionCallback{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void onSuccess(StepContext context, Object result){
			context.onSuccess(result);
		}

		@Override
		public void onFailure(StepContext context, Throwable t) {
			try {
                if (t instanceof FlowInterruptedException) {
                    for (CauseOfInterruption cause : ((FlowInterruptedException) t).getCauses()) {
                        if (cause instanceof CauseOfInterruption.UserInterruption) {
                            context.onFailure(t);
                            return;
                        }
                    }
                }
            } catch (Throwable p) {
                context.onFailure(p);
            }
        }		
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}