package com.terrier.boxcryptor.utils.yaml;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.terrier.utilities.automation.bundles.boxcryptor.objects.AbstractBCInventaireStructure;

public class BCInventaireYmlRepresenter extends Representer {

    @Override
    protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
            Object propertyValue, Tag customTag) {
        if (javaBean instanceof AbstractBCInventaireStructure 
        		&& 
        		("statutFichierChiffre".equals(property.getName())
        				|| "statutFichierClair".equals(property.getName()))) {
            return null;
        } else {
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }
}