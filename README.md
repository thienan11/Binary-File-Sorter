# Binary-File-Sorter
A Java program that sorts binary files using heap sort algorithm and the Proxy design pattern

# Background
The input data file consists of an arbitrary number of 4-byte sized records, with each record consisting of two 2-byte (a short) integer values ranging from 1 to 30,000. So each record is basically 2 shorts. The first 2-byte field is the key value (used for sorting) and the second 2-byte field contains a data value. The input file is guaranteed to be a multiple of 4096 bytes. All I/O operations will be done on blocks of size 4096 blocks (i.e., 1024 logical records).

# Overview
The program sorts the given binar file in ascending order, using a modified Heapsort algorithm. The heap "array" will be the file itself, instead of an array stored in memory.

# Design Pattern
- The **Proxy** pattern is used in the interaction between the Heap as viewed by the Heapsort algorithm, and the logical representation of the Heap as implemented by the disk file mediated by the Buffer Pool. The Buffer Pool is the "proxy" that controls access to the Random Access File. This means any time the Heap is reading from or writing to the binary file, it's actually going through the Buffer Pool.
