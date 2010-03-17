/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.interfaces.IAttribute;
import evopaint.pixel.attributes.ColorAttribute;
import evopaint.pixel.attributes.NeuronalAttribute;
import evopaint.pixel.attributes.PartnerSelectionAttribute;
import evopaint.pixel.attributes.RelationChoosingAttribute;
import evopaint.pixel.attributes.SpacialAttribute;
import java.awt.Point;
import java.util.IdentityHashMap;

/**
 *
 * @author tam
 */
public class Pixel {

    private ColorAttribute colorAttribute;
    private SpacialAttribute spacialAttribute;
    private NeuronalAttribute neuronalAttribute;
    private PartnerSelectionAttribute partnerSelectionAttribute;
    private RelationChoosingAttribute relationChoosingAttribute;

    public NeuronalAttribute getNeuronalAttribute() {
        return neuronalAttribute;
    }

    public void setNeuronalAttribute(NeuronalAttribute neuronalAttribute) {
        this.neuronalAttribute = neuronalAttribute;
    }

    public PartnerSelectionAttribute getPartnerSelectionAttribute() {
        return partnerSelectionAttribute;
    }

    public void setPartnerSelectionAttribute(PartnerSelectionAttribute partnerSelectionAttribute) {
        this.partnerSelectionAttribute = partnerSelectionAttribute;
    }

    public RelationChoosingAttribute getRelationChoosingAttribute() {
        return relationChoosingAttribute;
    }

    public void setRelationChoosingAttribute(RelationChoosingAttribute relationChoosingAttribute) {
        this.relationChoosingAttribute = relationChoosingAttribute;
    }

    public ColorAttribute getColorAttribute() {
        return colorAttribute;
    }

    public void setColorAttribute(ColorAttribute colorAttribute) {
        this.colorAttribute = colorAttribute;
    }

    public SpacialAttribute getLocation() {
        return spacialAttribute;
    }

    public void setLocation(SpacialAttribute location) {
        this.spacialAttribute = location;
    }

    public Pixel(ColorAttribute colorAttribute, SpacialAttribute spacialAttribute) {
        this.colorAttribute = colorAttribute;
        this.spacialAttribute = spacialAttribute;
    }


/*
    public void copy(Pixel pixie) {
        this.color = pixie.color;
        this.neuronalAttribute = pixie.neuronalAttribute.getCopy();
        this.partnerSelectionAttribute = pixie.partnerSelectionAttribute.getCopy();
        this.relationChoosingAttribute = pixie.partnerSelectionAttribute.getCopy();
    }

    public void clear(Config configuration) {
        this.color = configuration.backgroundColor;
        if (configuration.pixelRelationTypes.contains(NeuronalAttribute.class) {
            this.neuronalAttribute = new NeuronalAttribute(configuration., index)
        }
    }
*/
    


}
