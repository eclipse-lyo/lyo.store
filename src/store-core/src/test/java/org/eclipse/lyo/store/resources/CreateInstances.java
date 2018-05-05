package org.eclipse.lyo.store.resources;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;

public class CreateInstances {

    public static void doSomething()
    {
		try {
			
			//r1WithBlankResource is the resource to test and make sure it maintains its blank node.
			
			BlankResource aBlankResource = new BlankResource();
	    	aBlankResource.setIntProperty(1);
	    	WithBlankResource r1WithBlankResource = new WithBlankResource(new URI ("1"));
	    	r1WithBlankResource.setRelatesToBlankResource(aBlankResource);
	    	r1WithBlankResource.setStringProperty("some String");
	
            Model model1 = JenaModelHelper.createJenaModel(new WithBlankResource[] {r1WithBlankResource});

			//aWithTwoDepthBlankResource is the resource to test and make sure it maintains its blank node, and the blank node within the blank node?

	    	BlankResource anotherBlankResource = new BlankResource();
	    	anotherBlankResource.setIntProperty(1);
	    	WithBlankResource r2WithBlankResource = new WithBlankResource();
	    	r2WithBlankResource.setRelatesToBlankResource(anotherBlankResource);
	    	r2WithBlankResource.setStringProperty("some String");
	    	WithTwoDepthBlankResource aWithTwoDepthBlankResource = new WithTwoDepthBlankResource(new URI ("2"));
	    	aWithTwoDepthBlankResource.setIntProperty(1);
	    	aWithTwoDepthBlankResource.setRelatesToBlankResourceTwoDepth(r2WithBlankResource);

            Model model2 = JenaModelHelper.createJenaModel(new WithTwoDepthBlankResource[] {aWithTwoDepthBlankResource});

		} catch (Exception e) {
			e.printStackTrace();
		}

    }

}
