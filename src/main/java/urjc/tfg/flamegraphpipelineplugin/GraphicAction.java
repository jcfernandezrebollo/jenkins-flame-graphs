package urjc.tfg.flamegraphpipelineplugin;

import org.springframework.beans.factory.annotation.Autowired;

import hudson.model.Action;
import jenkins.model.*;

public class GraphicAction implements Action {
	
	public final static String ACTION_NAME = "Flame Graph";
	public final static String URL_NAME = "flamegraph";
	public String graphicName;
	
	@Autowired
	public GraphicAction(String graphicName) {
		this.graphicName = graphicName;
	}
	
	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return "graph.gif";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return ACTION_NAME;
	}

	@Override
	public String getUrlName() {
		
		return getRootUrl()+"plugin/flamegraph-pipeline-plugin/"+this.graphicName+".svg";
	}
		
	
	public String getJobNumber(){
		return Jenkins.getInstance().getRawWorkspaceDir();

	}
	
	 
	

	public String getRootUrl(){
		return Jenkins.getInstance().getRootUrl();
	}
    
}
