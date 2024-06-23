package eu.fbk.PapyGame.model.uml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class UMLParameter {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String type;
    @XmlAttribute
    private String direction;

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
