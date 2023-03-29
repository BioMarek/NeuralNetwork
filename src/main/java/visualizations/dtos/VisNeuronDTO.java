package visualizations.dtos;

import lombok.Data;

@Data
public class VisNeuronDTO implements Comparable<VisNeuronDTO> {
    public int name;
    public int position;
    public int layer;

    public VisNeuronDTO(int name, int layer) {
        this.name = name;
        this.layer = layer;
    }

    @Override
    public int compareTo(VisNeuronDTO neuronDTO) {
        if (this.name == neuronDTO.name)
            return 0;
        return (this.name < neuronDTO.name) ? -1 : 1;
    }
}
