package eu.fbk.PapyGame.model.uml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Model", namespace = "http://www.eclipse.org/uml2/5.0.0/UML")
@XmlAccessorType(XmlAccessType.FIELD)
public class UMLModel {
    @XmlElement(name = "packagedElement")
    private List<UMLPackagedElement> packagedElements;
    
    // Getters and setters
    public List<UMLPackagedElement> getPackagedElements() {
        return packagedElements;
    }

    public void setPackagedElements(List<UMLPackagedElement> packagedElements) {
        this.packagedElements = packagedElements;
    }
}
