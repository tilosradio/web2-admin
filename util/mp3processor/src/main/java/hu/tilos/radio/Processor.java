package hu.tilos.radio;

import java.io.*;

public class Processor {
    public static void main(String[] args) {
        new Processor().process(new File("/home/elek/projects/tilos/frontend/dist/www/testmp3/tilos-20140624-100000-121500.mp3"));
    }

    private void process(File file) {
        try (InputStream is = new FileInputStream(file)) {
            RingBuffer b = new RingBuffer(4);
            int i = 0, last = 0;
            while (i < Integer.MAX_VALUE) {
                int ch = is.read();
                b.add(ch);
                if (i>2000) {
                    break;
                }
                if (i > 3) {
                    if (b.get(0) == 255 &&
                            b.get(1) == 0xfa &&
                            (b.get(2) & 0xFD) == 0xD0 &&
                            (b.get(3) & 0xCF) == 0x44) {
                        System.out.println(String.format("%02x %02x %02x", b.get(1), b.get(2), b.get(3)));
                        System.out.println(i);
                        System.out.println(i - last);
                        System.out.println();


                        last = i;
                    }
                }

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
