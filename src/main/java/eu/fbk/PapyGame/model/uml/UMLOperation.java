package eu.fbk.PapyGame.model.uml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class UMLOperation {
    @XmlAttribute
    private String name;
    @XmlElement(name = "ownedParameter")
    private List<UMLParameter> parameters;

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<UMLParameter> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(List<UMLParameter> parameters) {
        this.parameters = parameters;
    }
}
