package eu.fbk.PapyGame.service.discoverer;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.springframework.stereotype.Service;

import jsondiscoverer.JsonSimpleDiscoverer;
import jsondiscoverer.JsonSource;
import jsondiscoverer.util.ModelHelper;

@Service
public class SimpleDiscovererService {
    public void simpleDiscoverer(String json, File file) {
        try {
            JsonSource jsonSource = new JsonSource("json");
            jsonSource.addJsonData(null, new StringReader(json));

            JsonSimpleDiscoverer discoverer = new JsonSimpleDiscoverer();
            EPackage ePackage = discoverer.discover(jsonSource);

            EList<EObject> eContents = ePackage.eContents();
            List<EObject> eContentsList = new ArrayList<>(eContents.size());
            eContentsList.addAll(eContents);

            ModelHelper.saveModel(eContentsList, file);
            // ModelHelper.saveEPackage(ePackage, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
