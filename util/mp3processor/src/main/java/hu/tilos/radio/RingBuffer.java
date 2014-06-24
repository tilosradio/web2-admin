package hu.tilos.radio;

public class RingBuffer {
    private int idx;
    private int[] data;
    private int size;

    public RingBuffer(int size) {
        this.size = size;
        data = new int[size];
    }

    public void add(int value) {
        data[idx++] = value;
        if (idx == size) {
            idx = 0;
        }
    }

    public int get(int r) {
        int realIndex = idx - size + r;
        if (realIndex < 0) {
            realIndex += size;
        }
        return data[realIndex];
    }
}
