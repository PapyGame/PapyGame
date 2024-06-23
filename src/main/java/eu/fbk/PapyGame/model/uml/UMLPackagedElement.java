package eu.fbk.PapyGame.model.uml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class UMLPackagedElement {
    @XmlAttribute
    private String name;

    @XmlElement(name = "ownedAttribute")
    private List<UMLAttribute> attributes;

    @XmlElement(name = "ownedOperation")
    private List<UMLOperation> operations;

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<UMLAttribute> getAttributes() {
        return attributes;
    }

    public List<UMLOperation> getOperations() {
        return operations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttributes(List<UMLAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setOperations(List<UMLOperation> operations) {
        this.operations = operations;
    }

    
}
