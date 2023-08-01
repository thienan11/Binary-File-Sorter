package externalsort;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for BufferPool
 */
class TestBufferPool {
    private final String fileName = "bufferpool_test.dat";
    @Test
    void testSettingRecordAt() throws IOException{
        Utils.generateByteFile(1024, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(1, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 30));
            FileRecord rec = bp.getRecordAt(0);

            assertEquals(10, rec.key());
            assertEquals(1, bp.getCacheHits());
            assertEquals(0, bp.getCacheMisses());
            assertEquals(0, bp.getDiskReads());
            assertEquals(0, bp.getDiskWrites());

            assertEquals(1024, bp.getNumRecord());
            bp.flushAll();
        } catch (Exception e){
            System.out.println("ERROR");
        }
    }

    @Test
    void testSettingRecordAtIndexNotInBuffer() throws IOException{
        Utils.generateByteFile(2048, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(1, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 30));
            FileRecord rec = bp.getRecordAt(0);
            bp.getRecordAt(2000);

            assertEquals(10, rec.key());
            bp.flushAll();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    @Test
    void testGettingRecordAtBlockOutsideOfBuffer() throws IOException{
        Utils.generateByteFile(2048, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(2, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 30));
            FileRecord rec = bp.getRecordAt(0);
            bp.getRecordAt(2000);

            assertEquals(10, rec.key());
            bp.flushAll();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    @Test
    void testSettingRecordToNewRecord() throws IOException{
        Utils.generateByteFile(2048, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(2, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 30));
            assertEquals(10, bp.getRecordAt(0).key());

            bp.setRecordAt(0, new FileRecord((short) 30, (short) 60));
            assertEquals(30, bp.getRecordAt(0).key());

            bp.flushAll();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    @Test
    void testSettingRecordWhenFull() throws IOException{
        Utils.generateByteFile(2048, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(1, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 30));
            assertEquals(10, bp.getRecordAt(0).key());

            bp.setRecordAt(2000, new FileRecord((short) 30, (short) 60));
            assertEquals(30, bp.getRecordAt(2000).key());

            bp.flushAll();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    @Test
    void testSwap() throws IOException{
        Utils.generateByteFile(1024, fileName);
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")){
            BufferPool bp = new BufferPool(1, raf);
            bp.setRecordAt(0, new FileRecord((short) 10, (short) 20));
            bp.setRecordAt(50, new FileRecord((short) 30, (short) 40));

            assertEquals(10, bp.getRecordAt(0).key());
            assertEquals(30, bp.getRecordAt(50).key());

            bp.swap(0, 50);

            assertEquals(10, bp.getRecordAt(50).key());
            assertEquals(30, bp.getRecordAt(0).key());

            bp.flushAll();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

}
