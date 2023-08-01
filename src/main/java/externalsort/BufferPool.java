package externalsort;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Represents the buffer pool
 *
 * @author Thien An Tran
 * @version June 8, 2023
 */
public class BufferPool {
    private final LinkedList<BufferNode> bufferList;
    private final RandomAccessFile file;
    private final int numBuffers;
    private int cacheHits;
    private int cacheMisses;
    private int diskReads;
    private int diskWrites;
    private BufferNode currBlock;

    /**
     * Constructs a BufferPool structure
     *
     * @param numBuffers an int of how many blocks the buffer pool can hold
     * @param raf a RandomAccessFile
     */
    public BufferPool(int numBuffers, RandomAccessFile raf) {
        this.bufferList = new LinkedList<>();
        this.numBuffers = numBuffers;
        this.file = raf;
        this.cacheHits = 0;
        this.cacheMisses = 0;
        this.diskReads = 0;
        this.diskWrites = 0;
    }

    /**
     * Gets the number of records in the file
     *
     * @throws IOException when a file cannot be accessed
     */
    public int getNumRecord() throws IOException {
        return (int) file.length() / 4;
    }

    /**
     * Gets the number of cache hits
     *
     * @return an int
     */
    public int getCacheHits() {
        return cacheHits;
    }

    /**
     * Gets the number of cache misses
     *
     * @return an int
     */
    public int getCacheMisses() {
        return cacheMisses;
    }

    /**
     * Gets the number of disk reads
     *
     * @return an int
     */
    public int getDiskReads() {
        return diskReads;
    }

    /**
     * Gets the number of disk writes
     *
     * @return an int
     */
    public int getDiskWrites() {
        return diskWrites;
    }

    /**
     * Creates a new buffer block based on the given record index by going to the file
     *
     * @param index an int
     *
     * @return a BufferNode
     * @throws IOException when the file cannot be accessed
     */
    public BufferNode loadNewBuffer(int index) throws IOException {
        return new BufferNode(index, file);
    }

    /**
     * Removes the last buffer block in the buffer pool and write it back if it was changed
     *
     * @throws IOException when a file cannot be accessed
     */
    public void flush() throws IOException {
        BufferNode removedBuff = bufferList.removeLast();
        if (removedBuff.isModified()) {
            diskWrites++;
            removedBuff.write();
        }
    }

    /**
     * Flushes out all the buffer blocks in the buffer pool
     *
     * @throws IOException when a file cannot be accessed
     */
    public void flushAll() throws IOException{
        while (!bufferList.isEmpty()) {
            BufferNode removedBuff = bufferList.remove();
            if (removedBuff.isModified()) {
                diskWrites++;
                removedBuff.write();
            }
        }
    }

    /**
     * Checks if the buffer pool is full or not
     *
     * @return true if it is full, false otherwise
     */
    public boolean isFull(){
        return bufferList.size() == numBuffers;
    }

    /**
     * Checks if the record at a given index is in a block in the buffer pool
     *
     * @param idx an int of the index of the record
     * @return true if it is in the buffer pool, false otherwise
     */
    public boolean isInBufferPool(int idx){
        for (BufferNode bufferNode : bufferList) {
            if (bufferNode.isInBlock(idx)) {
                currBlock = bufferNode;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the record at a given index in the buffer node
     *
     * @param idx an int, the index of the record
     * @return a FileRecord
     * @throws IOException when a file cannot be accessed
     */
    public FileRecord getRecordAt(int idx) throws IOException {
        FileRecord res;

        if (isInBufferPool(idx)) {
            cacheHits++;

            BufferNode bufferNode = currBlock;

            bufferList.remove(bufferNode);
            bufferList.addFirst(bufferNode);

            res = bufferNode.getRecord(idx);

        } else {
            cacheMisses++;
            diskReads++;

            if (isFull()) {
                flush();
                BufferNode bufferNode = loadNewBuffer(idx);
                bufferList.addFirst(bufferNode);

                res = bufferNode.getRecord(idx);
            } else {
                BufferNode bufferNode = loadNewBuffer(idx);
                bufferList.addFirst(bufferNode);

                res = bufferNode.getRecord(idx);
            }
        }
        return res;
    }

    /**
     * Sets the record at a given index in the buffer node
     *
     * @param idx an int, the index of the record to set
     * @param rec a FileRecord
     * @throws IOException when a file cannot be accessed
     */
    public void setRecordAt(int idx, FileRecord rec) throws IOException {
        if (isInBufferPool(idx)) {
            BufferNode bufferNode = currBlock;
            bufferNode.setRecordAtIdx(idx, rec);

            bufferList.remove(bufferNode);
            bufferList.addFirst(bufferNode);

            bufferNode.markAsModified();
        } else {

            if (isFull()) {
                flush();
                BufferNode bufferNode = loadNewBuffer(idx);
                bufferNode.setRecordAtIdx(idx, rec);

                bufferList.addFirst(bufferNode);

                bufferNode.markAsModified();
            } else {
                BufferNode bufferNode = loadNewBuffer(idx);
                bufferNode.setRecordAtIdx(idx, rec);

                bufferList.addFirst(bufferNode);

                bufferNode.markAsModified();
            }
        }
    }

    /**
     * Swaps two values given their indices
     *
     * @param idx1 an int, the index of the first value
     * @param idx2 an int, the index of the second value
     * @throws IOException when a file cannot be accessed
     */
    public void swap(int idx1, int idx2) throws IOException {
        FileRecord rec1 = new FileRecord(getRecordAt(idx1).key(), getRecordAt(idx1).value());
        FileRecord rec2 = new FileRecord(getRecordAt(idx2).key(), getRecordAt(idx2).value());

        setRecordAt(idx1, rec2);
        setRecordAt(idx2, rec1);
    }
}
