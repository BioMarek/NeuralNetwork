package Utils;

import lombok.Data;

@Data
public class Pair<T> {

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    T first;
    T second;
}
