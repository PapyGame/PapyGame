package eu.fbk.PapyGame.model.uml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class UMLAttribute {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String type;

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
