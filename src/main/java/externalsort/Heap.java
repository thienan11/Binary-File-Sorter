package externalsort;

import java.io.IOException;

/**
 * Represents a max heap
 *
 * @author Thien An Tran
 * @version June 8, 2023
 */
public class Heap {
    private final BufferPool bp;
    private final int maxSize;
    private int currSize;

    /**
     * Constructs a heap structure
     *
     * @param bp a buffer pool object
     * @throws IOException when a file cannot be accessed
     */
    public Heap(BufferPool bp) throws IOException {
        this.bp = bp;
        this.maxSize = bp.getNumRecord();
        this.currSize = this.maxSize;
    }

    /**
     * Builds a max heap
     *
     * @throws IOException when a file cannot be accessed
     */
    public void buildMaxHeap() throws IOException {
        for (int i = (currSize - 1) / 2; i >= 0; i--){
            heapifyDown(i);
        }
    }

    /**
     * Sorts the heap in ascending order
     *
     * @throws IOException when a file cannot be accessed
     */
    public void heapSort() throws IOException {
        buildMaxHeap();
        for (int i = maxSize - 1; i > 0; i--) {
            currSize--;
            bp.swap(0, i);
            heapifyDown(0);

            bp.getRecordAt(currSize);
        }

    }

    /**
     * Sifts down the value at a given index to maintain the max heap property
     *
     * @param index the index of the value to sift down
     * @throws IOException when a file cannot be accessed
     */
    private void heapifyDown(int index) throws IOException {
        int largest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < currSize && bp.getRecordAt(left).key() > bp.getRecordAt(largest).key()) {
            largest = left;
        }

        if (right < currSize && bp.getRecordAt(right).key() > bp.getRecordAt(largest).key()) {
            largest = right;
        }

        if (largest != index) {
            bp.swap(index, largest);
            heapifyDown(largest);
        }
    }
}