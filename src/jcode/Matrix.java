package jcode;

public class Matrix {
    private int[][] matrix;
    public final int n;
    public final int m;

    public Matrix(int n,int m){
        matrix = new int[n][m];
        this.n = n;
        this.m = m;
    }

    public Matrix(int n) {
        matrix = new int[n][n];
        this.n = n;
        this.m = n;
        for (int i = 0; i < n; i++) {
            matrix[i][n - i - 1] = 1;
        }
    }

    public int getElement(int x,int y) throws ArrayIndexOutOfBoundsException{
        return matrix[x][y];
    }

    public void setElement(int x,int y,int val) throws ArrayIndexOutOfBoundsException{
        matrix[x][y]=val;
    }

    public Matrix horizontalRotate() {
        Matrix b = new Matrix(n);
        return Matrix.mul(b,this);
    }

    public Matrix verticalRotate() {
        Matrix b = new Matrix(m);
        return Matrix.mul(this,b);
    }

    public void fill(int[][] src) throws ArrayIndexOutOfBoundsException{
        for(int i=0;i<n;i++){
            if (m >= 0) System.arraycopy(src[i], 0, matrix[i], 0, m);
        }
    }

    public static Matrix mul(Matrix x,Matrix y){
        if(x.m!=y.n) return null;
        Matrix res = new Matrix(x.n,y.m);
        for (int i = 0; i < x.n; i++) {
            for (int j = 0; j < y.m; j++) {
                for (int k = 0; k < x.m; k++) {
                    res.setElement(i,j,res.getElement(i,j)+x.getElement(i,k) * y.getElement(k,j));
                }
            }
        }
        return res;
    }

    public void print(boolean x){

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                if(x){
                    if (matrix[i][j] == 1) System.out.print("⬛");
                    else System.out.print("⬜");
                } else {
                    System.out.print(matrix[i][j]+"\t");
                }
            }
            System.out.println();
        }
    }
}
