package ecologylab.sensors.gps;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LightBufBucket extends ByteArrayOutputStream {
    int newline=-1;
    /**
     Reads from input stream till there is no more data to read
     I could also halt reading when a byte with 13 is recieved ??
     */
    public void read(InputStream in,int bytesToRead) throws IOException{
        byte[] buffer=new byte[bytesToRead];
        int read=0;
        read=in.read(buffer);
        write(buffer,0,read);
        
    }
    /**
     Overrides the basic write to force the write as data,0,data.length
     */
    public void write(byte[] data) {
        this.write(data,0,data.length);
        
    }
    /**
     Overrides the basic write does inline checking to see if the return character is
     recieved...
     */
    public void write(byte[] data,int offset,int length) {
        for(int x=offset;x<(offset+length) && x<data.length;x++) {
            if (data[x]==13)
                newline=this.count+x;
        }
        super.write(data,0,data.length);
    }
    public void write(int data) {
        write(new byte[]{(byte)data});
    }
    public boolean hasNewLine() {return newline>=0;}
    public int getNewLine() {return newline;}
    public void reset() {
        newline=-1;
        super.reset();
    }
    public static void main(String[] args) {
        LightBufBucket lfb=new LightBufBucket();
        byte[] inputBuffer=new byte[100];
        String answer="";
        try {
            while(!answer.equals("end")) {
                int len = System.in.read(inputBuffer);
                answer = new String(inputBuffer, 0, len);
                byte[]data =answer.getBytes();
                for (int x=0;x <data.length;x++) {
                    lfb.write(data[x]);
                    if (lfb.hasNewLine()) {
                        System.out.println("newLine at"+lfb.getNewLine()+"\n"+lfb.toString());
                    }
                }
                lfb.reset();
            }
        } catch (IOException err) {err.printStackTrace();}
    }
}
