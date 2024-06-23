package eu.fbk.PapyGame.service.discoverer;


import java.io.File;
import java.io.StringReader;

import org.eclipse.emf.ecore.EPackage;
import org.springframework.stereotype.Service;

import jsondiscoverer.JsonAdvancedDiscoverer;
import jsondiscoverer.JsonSource;
import jsondiscoverer.JsonSourceSet;
import jsondiscoverer.util.ModelHelper;

@Service
public class AdvancedDiscovererService {
    public void advancedDiscoverer(String json, File file) {
        try {
            JsonSource jsonSource = new JsonSource("json");
            jsonSource.addJsonData(null, new StringReader(json));

            JsonSourceSet jsonSourceSet = new JsonSourceSet("studentJson");
            jsonSourceSet.addJsonSource(jsonSource);

            JsonAdvancedDiscoverer jsonAdvancedDiscoverer = new JsonAdvancedDiscoverer(jsonSourceSet);
            EPackage ePackage = jsonAdvancedDiscoverer.discover();

            ModelHelper.saveEPackage(ePackage, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
