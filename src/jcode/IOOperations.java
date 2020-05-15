package jcode;

import java.io.*;

public class IOOperations {
    public static int writeToFile(Matrix m,String filename) {
        try {
            File f = new File(filename);
            if (f.exists() && f.isFile())f.delete();
            f.createNewFile();
            BufferedWriter fw = new BufferedWriter(new FileWriter(f));
            long x = (long) m.n << 32 | m.m;
            fw.write(Long.toString(x));
            fw.newLine();
            for(int i=0;i<m.n;i++){
                char out=0;
                for(int j=0;j<m.m;j++){
                    int offset = j%16;
                    if(m.getElement(i,j)==1){
                        out |= (1<<offset);
                    }
                    if(offset==15 || j==m.m-1){
                        fw.write(out);
                        out=0;
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Matrix readFromFile(String filename) throws IOException {
        Matrix matrix = null;
            File f = new File(filename);
            BufferedReader fr = new BufferedReader(new FileReader(f));
            String s = fr.readLine();
            long x = Long.parseLong(s);
            int n = (int)(x >> 32);
            int m = (int)(x);
            int countInRow = m/16;
            if(m%16!=0)countInRow++;
            matrix = new Matrix(n,m);
            for(int i=0;i<n;i++){
                for(int j=0;j<countInRow;j++){
                    int c = fr.read();
                    for(int k=0;k<16;k++){
                        if(j*16+k==m)break;
                        if(((c>>k)&1)==1){
                            matrix.setElement(i,j*16+k,1);
                        }
                    }
                }
            }
            fr.close();
        return matrix;
    }
}
