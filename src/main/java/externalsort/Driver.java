package externalsort;

import java.io.*;

/**
 * A package for Heap-sorting large binary files.
 *
 * @author Thien An Tran (ttran259@calpoly.edu)
 * @version June 8, 2023
 */
public class Driver {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                """
                Expected exactly 2 arguments. One for a file name to sort, and one for the number of buffers in
                the buffer pool.
                """
            );
        }

        String fileName = args[0];
        int bufferSize = Integer.parseInt(args[1]);

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            BufferPool bufferPool = new BufferPool(bufferSize, raf);
            Heap heap = new Heap(bufferPool);

            long start = System.currentTimeMillis();

            // start sort and flush all at the end
            heap.heapSort();
            bufferPool.flushAll();

            long end = System.currentTimeMillis();

            // get stats from buffer pool
            int cacheHits = bufferPool.getCacheHits();
            int cacheMisses = bufferPool.getCacheMisses();
            int diskReads = bufferPool.getDiskReads();
            int diskWrites = bufferPool.getDiskWrites();

            printContents(raf);

            System.out.println("\nSTATS");
            System.out.println("File name: " + fileName);
            System.out.println("Cache hits: " + cacheHits);
            System.out.println("Cache misses: " + cacheMisses);
            System.out.println("Disk reads: " + diskReads);
            System.out.println("Disk writes: " + diskWrites);
            System.out.println("Time to sort: " + (end - start) + " ms");
            if(Utils.checkFile(fileName)){
                System.out.println("FILE SORTED");
            } else {
                System.out.println("FILE NOT SORTED");
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    public static void printContents(RandomAccessFile raf) throws IOException {
        long fileSize = raf.length();

        int numBlocks = (int) (fileSize / 4096);
        for (int block = 0; block < numBlocks; block++) {
            int recordOffset = block * 4096;
            raf.seek(recordOffset);
            short key = raf.readShort();
            short value = raf.readShort();
            System.out.print(key + " " + value + "     ");
        }
    }
}
