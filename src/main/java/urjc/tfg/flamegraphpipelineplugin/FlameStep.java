package urjc.tfg.flamegraphpipelineplugin;

import java.util.Collections;
import java.util.Set;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import urjc.tfg.flamegraphpipelineplugin.*;
import hudson.Extension;
import hudson.model.TaskListener;




public class FlameStep extends Step{

	public static final String STEP_NAME = "flame";
	private String args;
	private int seconds;
	private String flameType;

	@DataBoundConstructor
	public FlameStep(String args, int seconds, String flameType) {
		this.args = args;
		this.seconds = seconds;
		this.flameType = flameType;
	}
	
	public String getArgs(){
		return this.args;
	}
	
	@DataBoundSetter 
	public void setArgs(String args){
		this.args = args;
	}

	public int getSeconds(){
		return this.seconds;
	}
	
	@DataBoundSetter
	public void setSeconds(int seconds){
		this.seconds = seconds;
	}
	
	public String getFlameType(){
		return this.flameType;
	}
	
	@DataBoundSetter 
	public void setFlameType(String flameType){
		this.flameType = flameType;
	}
	
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
	
  
	@Override
	public StepExecution start(StepContext context) throws Exception {
		return new FlameStepExecution(this, context);
	}	

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public String getFunctionName() {
            return STEP_NAME;
        }

        @Override
        public String getDisplayName() {
        	return "Run build steps in FlameGraph";
        }

		@Override
		public Set<Class<TaskListener>> getRequiredContext() {
            return Collections.singleton(TaskListener.class);

		}

    }
    
    
//    posible ampliacion 
    
//	@DataBoundSetter
//	private String jobName;
//	@DataBoundSetter
//	private String jobAction;
	//Argumentos del FlameGraph
//	private String tittle;
//	private int width;
//	private int height;
//	private int minwidth;
//	private String fontType;
//	private int fontSize;
//	private String countName;
//	private String nameType;
//	private String colors;
//	private Boolean hash;
//	private Boolean cp;
//	private Boolean reverse;
//	private Boolean inverted;
//	private Boolean negate;
    
//	public String getJobName(){
//		return jobName;
//	}
//	
//	public String getJobAction(){	
//		return jobAction;
//	}
	

	
//	@DataBoundSetter 
//	public void setJobName(String jobName){
//		this.jobName = jobName;
//	}
//	
//	@DataBoundSetter 
//	public void setJobAction(String jobAction){
//		this.jobAction = jobAction;
//		
//	}
	

    
 // CON ESTE PARECE QUE FUNCIONABA
// 	@Extension
// 	public static class DescriptorImpl extends AbstractStepDescriptorImpl{
// 		
// 		public DescriptorImpl(){
// 			super(FlameStepExecution.class);
// 		}
// 		
// 		public DescriptorImpl(Class<? extends StepExecution> executionType){
// 			super(executionType);
// 		}
 //
// 		@Override
// 		public String getFunctionName() {
// 			return STEP_NAME;
// 		}
// 		
// 		@Override
// 		public String getDisplayName(){
// 			return "Run build steps in FlameGraph";
// 		}
// 		
// 		@Override
// 		public boolean takesImplicitBlockArgument(){
// 			return true;
// 		}
// 		
// 		@Override
// 		public boolean isAdvanced(){
// 			return true;
// 		}
 //	
// 	}
}

