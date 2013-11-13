package com.denimgroup.threadfix.framework;

import static com.denimgroup.threadfix.framework.TestConstants.BODGEIT_SOURCE_LOCATION;
import static com.denimgroup.threadfix.framework.TestConstants.BODGEIT_WEB_XML;
import static com.denimgroup.threadfix.framework.TestConstants.PETCLINIC_SOURCE_LOCATION;
import static com.denimgroup.threadfix.framework.TestConstants.PETCLINIC_WEB_XML;
import static com.denimgroup.threadfix.framework.TestConstants.WAVSEP_SOURCE_LOCATION;
import static com.denimgroup.threadfix.framework.TestConstants.WAVSEP_WEB_XML;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.denimgroup.threadfix.framework.engine.ProjectDirectory;
import com.denimgroup.threadfix.framework.engine.ServletMappings;
import com.denimgroup.threadfix.framework.engine.WebXMLParser;
import com.denimgroup.threadfix.framework.enums.FrameworkType;

public class WebXMLParserTests {

    ServletMappings vulnClinic = WebXMLParser.getServletMappings(new File(PETCLINIC_WEB_XML),
    		new ProjectDirectory(new File(PETCLINIC_SOURCE_LOCATION)));
	ServletMappings wavsep = WebXMLParser.getServletMappings(new File(WAVSEP_WEB_XML),
    		new ProjectDirectory(new File(WAVSEP_SOURCE_LOCATION)));
	ServletMappings bodgeIt = WebXMLParser.getServletMappings(new File(BODGEIT_WEB_XML),
    		new ProjectDirectory(new File(BODGEIT_SOURCE_LOCATION)));
	
    ////////////////////////////////////////////////////////////////
    ///////////////////////////// Tests ////////////////////////////
    ////////////////////////////////////////////////////////////////
    
    public void testFindWebXML() {
    	String[]
    			sourceLocations = { PETCLINIC_SOURCE_LOCATION, WAVSEP_SOURCE_LOCATION, BODGEIT_SOURCE_LOCATION },
    			webXMLLocations = { PETCLINIC_WEB_XML, WAVSEP_WEB_XML, BODGEIT_WEB_XML };
    	
    	for (int i = 0; i < sourceLocations.length; i++) {
    		File projectDirectory = new File(sourceLocations[i]);
    		assertTrue(projectDirectory != null && projectDirectory.exists());
    		
    		File file = new ProjectDirectory(projectDirectory).findWebXML();
    		assertTrue(file.getName().equals("web.xml"));
    		
    		assertTrue(file.getAbsolutePath().equals(webXMLLocations[i]));
    	}
    }
    
    // TODO improve these tests.
    @Test
    public void testWebXMLParsing() {
    	assertTrue(vulnClinic.getClassMappings().size() == 2);
    	assertTrue(vulnClinic.getServletMappings().size() == 2);
    	
    	assertTrue(wavsep.getClassMappings().size() == 0);
    	assertTrue(wavsep.getServletMappings().size() == 0);
    	
    	assertTrue(bodgeIt.getClassMappings().size() == 0);
    	assertTrue(bodgeIt.getServletMappings().size() == 1);
    }
    
    @Test
    public void testTypeGuessing() {
    	assertTrue(vulnClinic.guessApplicationType() == FrameworkType.SPRING_MVC);
    	assertTrue(wavsep.guessApplicationType() == FrameworkType.JSP);
    	assertTrue(bodgeIt.guessApplicationType() == FrameworkType.JSP);
    }
    
    @Test
    public void testBadInput() {
    	assertTrue(new ProjectDirectory(null).findWebXML() == null);
    	ServletMappings nullInputMappings = WebXMLParser.getServletMappings(null, null);
    	assertTrue(nullInputMappings != null);
    	assertTrue(nullInputMappings.getClassMappings() == null);
    	assertTrue(nullInputMappings.getServletMappings() == null);
    	
    	File doesntExist = new File("This/path/doesnt/exist");
    	
    	assertTrue(new ProjectDirectory(doesntExist).findWebXML() == null);
    	
    	
    }
}