package externalsort;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Represents a buffer block
 *
 * @author Thien An Tran
 * @version June 8, 2023
 */
public class BufferNode {
    private final byte[] block;
    private final RandomAccessFile file;
    private final int blockIdx;
    private boolean modified;
    private static final int BYTE_PER_BLOCK = 4096;
    private static final int RECORD_PER_BLOCK = 1024;

    /**
     * Constructs a BufferNode object
     *
     * @param recordIdx an index of the record
     * @param raf a RandomAccessFile
     * @throws IOException when a file cannot be accessed
     */
    public BufferNode(int recordIdx, RandomAccessFile raf) throws IOException {
        this.file = raf;
        this.modified = false;
        this.block = new byte[BYTE_PER_BLOCK];
        this.blockIdx = (recordIdx / RECORD_PER_BLOCK) * BYTE_PER_BLOCK;

        this.file.seek(blockIdx);
        this.file.read(block);
    }

    /**
     * Sets a record at a given index in the buffer block
     *
     * @param recordIdx an int, the index of record to set
     * @param rec a FileRecord to set
     */
    public void setRecordAtIdx(int recordIdx, FileRecord rec) {
        int index = recordIdx % RECORD_PER_BLOCK;
        ByteBuffer bb = ByteBuffer.wrap(block);
        bb.position(index * 4);
        bb.putShort(rec.key());
        bb.putShort(rec.value());
    }

    /**
     * This method marks if this buffer is changed
     */
    public void markAsModified() {
        modified = true;
    }

    /**
     * Determines whether this buffer node has been modified or not
     *
     * @return true if this block has been modified, false otherwise
     */
    public boolean isModified(){
        return modified;
    }

    /**
     * This writes back to the file if the buffer has changed
     *
     * @throws IOException when a file cannot be accessed
     */
    public void write() throws IOException {
        file.seek(blockIdx);
        file.write(block);
        modified = false;
    }

    /**
     * Determines if a particular record is in this buffer block based on the given index
     *
     * @param recordIdx the index of the record.
     * @return true if the record is in this block, false otherwise.
     */
    public boolean isInBlock(int recordIdx) {
        int byteOffset = recordIdx * 4;
        return (blockIdx <= byteOffset && byteOffset < blockIdx + BYTE_PER_BLOCK);
    }

    /**
     * Get a FileRecord at a given index
     *
     * @param recordIdx the index of the record to get
     * @return a FileRecord
     */
    public FileRecord getRecord(int recordIdx) {
        if (isInBlock(recordIdx)) {
            int index = recordIdx % RECORD_PER_BLOCK;
            ByteBuffer bb = ByteBuffer.wrap(block);
            bb.position(index * 4);
            short key = bb.getShort();
            short value = bb.getShort();
            return new FileRecord(key, value);
        } else {
            return null;
        }
    }
}
