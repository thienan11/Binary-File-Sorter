package externalsort;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

class TestHeapSorting {

    private final String fileName = "sort_test.dat";

    @Test
    void test1BufferSizeAnd1BlockInFile() throws IOException{
        Utils.generateByteFile(1024, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(1, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test1BufferSizeAnd4BlockInFile() throws IOException{
        Utils.generateByteFile(4096, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(1, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));
            assertNotEquals(4, bp.getCacheMisses());
            assertNotEquals(4, bp.getDiskReads());
            assertNotEquals(4, bp.getDiskWrites());

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test4BufferSizeAnd4BlockInFile() throws IOException{
        Utils.generateByteFile(4096, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(4, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));
            assertEquals(4, bp.getCacheMisses());
            assertEquals(4, bp.getDiskReads());
            assertEquals(4, bp.getDiskWrites());

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test1BufferSizeAnd10BlockInFile() throws IOException{
        Utils.generateByteFile(10240, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(1, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test10BufferSizeAnd10BlockInFile() throws IOException{
        Utils.generateByteFile(10240, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(10, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));
            assertEquals(10, bp.getCacheMisses());
            assertEquals(10, bp.getDiskReads());
            assertEquals(10, bp.getDiskWrites());

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test10BufferSizeAnd4BlockInFile() throws IOException{
        Utils.generateByteFile(4096, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(10, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));
            assertEquals(4, bp.getCacheMisses());
            assertEquals(4, bp.getDiskReads());
            assertEquals(4, bp.getDiskWrites());

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test10BufferSizeAnd20BlockInFile() throws IOException{
        Utils.generateByteFile(20480, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(10, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test1BufferSizeAnd20BlockInFile() throws IOException{
        Utils.generateByteFile(20480, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(1, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void test2BufferSizeAnd3BlockInFile() throws IOException{
        Utils.generateByteFile(3072, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertFalse(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(2, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void testSortedFile() {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){

            assertTrue(Utils.checkFile(fileName));

            BufferPool bp = new BufferPool(3, raf);
            Heap heap = new Heap(bp);

            heap.heapSort();
            bp.flushAll();

            assertTrue(Utils.checkFile(fileName));

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }
}
