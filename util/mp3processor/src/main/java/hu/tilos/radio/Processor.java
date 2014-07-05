package hu.tilos.radio;

import java.io.*;

public class Processor {
    public static void main(String[] args) {
        new Processor().process(new File("/home/elek/projects/tilos/tilosradio-20140403-0830.mp3"));
    }

    private void processOld(File file) {
        try (InputStream is = new FileInputStream(file)) {
            RingBuffer b = new RingBuffer(4);
            int i = 0, last = 0;
            while (i < Integer.MAX_VALUE) {
                int ch = is.read();
                b.add(ch);
                if (i > 2000) {
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

    private void process(File file) {
        try (InputStream is = new FileInputStream(file)) {
            RingBuffer last = findFirstFrame(is);
            RingBuffer b = new RingBuffer(10);
            int start = 55000000;
            try (InputStream prev = new FileInputStream(new File("/home/elek/projects/tilos/tilosradio-20140403-0800.mp3"))) {
                System.out.println("Skipped " + prev.skip(start) + " bytes");
                int position = start;
                int ch;
                while ((ch = prev.read()) != -1) {
                    b.add(ch);
                    if (isFrameStart(b)) {
                        if (b.equals(last)) {
                            System.out.println("Bingo " + position);
                        }
                    }
                    if (position % 1000000 == 0) {
                        System.out.println(position);
                    }
                    position++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RingBuffer findFirstFrame(InputStream is) throws IOException {
        RingBuffer b = new RingBuffer(10);
        int i = 0, last = 0;
        while (i < Integer.MAX_VALUE) {
            int ch = is.read();
            b.add(ch);
            if (i > 2000) {
                break;
            }
            if (i > 3) {
                if (isFrameStart(b)) {
                    System.out.println(String.format("%02x %02x %02x", b.get(1), b.get(2), b.get(3)));
                    System.out.println(i - b.getSize());
                    System.out.println(i - last);
                    System.out.println();


                    return b;
                }
            }

            i++;
        }
        return b;
    }

    private boolean isFrameStart(RingBuffer b) {
        return b.get(0) == 255 &&
                b.get(1) == 0xfa &&
                (b.get(2) & 0xFD) == 0xD0 &&
                (b.get(3) & 0xCF) == 0x44;
    }
}
