package jcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoFunc {

    public static Matrix generateStencil(int n,int m) throws Exception{
        int[][] matrix = new int[n][m];
        while (!getStencil(matrix,n,m)) {
            matrix = new int[n][m];
        }
        Matrix res = new Matrix(n,m);
        res.fill(matrix);
        return res;
    }

    public static Matrix fillMatrixByRotating(Matrix stencil,boolean verticalDirection){
        Matrix retMatrix = new Matrix(stencil.n,stencil.m);
        if(retMatrix==null)return null;
        int i = 1;
        if(verticalDirection){
            i = fillStepVertical(retMatrix, i, stencil);
            i = fillStepVertical(retMatrix, i, stencil.horizontalRotate().verticalRotate());
            i = fillStepVertical(retMatrix, i, stencil.horizontalRotate());
            fillStepVertical(retMatrix, i, stencil.verticalRotate());
        } else {
            i = fillStepHorizontal(retMatrix, i, stencil);
            i = fillStepHorizontal(retMatrix, i, stencil.horizontalRotate().verticalRotate());
            i = fillStepHorizontal(retMatrix, i, stencil.horizontalRotate());
            fillStepHorizontal(retMatrix, i, stencil.verticalRotate());
        }

        return retMatrix;
    }

    public static boolean checkStencil(Matrix stencil){
        int countNotEmpty = stencil.n*stencil.m/4;
        for(int i=0;i<stencil.n;i++){
            for(int j=0;j<stencil.m;j++){
                if(stencil.getElement(i,j)==1)countNotEmpty--;
            }
        }
        if(countNotEmpty!=0)return false;
        Matrix fill = fillMatrixByRotating(stencil,true);
        for(int i=0;i<fill.n;i++){
            for(int j=0;j<fill.m;j++){
                if(fill.getElement(i,j)==0)return false;
            }
        }
        return true;
    }

    private static int fillStepHorizontal(Matrix resMatrix, int startIndex, Matrix stencil){
        for (int i = 0; i < stencil.n; i++) {
            for (int j = 0; j < stencil.m; j++) {
                if (stencil.getElement(i,j) == 1) {
                    resMatrix.setElement(i,j, startIndex);
                    startIndex++;
                }
            }
        }
        return startIndex;
    }

    private static int fillStepVertical(Matrix resMatrix, int startIndex, Matrix stencil) {
        for (int j = 0; j < stencil.m; j++) {
            for (int i = 0; i < stencil.n; i++) {
                if (stencil.getElement(i,j) == 1) {
                    resMatrix.setElement(i,j, startIndex);
                    startIndex++;
                }
            }
        }
        return startIndex;
    }

    public static Matrix getEncryptMatrix(Matrix matrix){
        int N = matrix.n*matrix.m;
        int n = matrix.m;
        Matrix retMatrix = new Matrix(N,N);
        for(int i=0;i<N;i++){
            retMatrix.setElement(matrix.getElement(i/n,i%n)-1,i,1);
        }
        return retMatrix;
    }

    public static Matrix getDecryptMatrix(Matrix matrix){
        int N = matrix.n*matrix.m;
        int n = matrix.m;
        Matrix retMatrix = new Matrix(N,N);
        for(int i=0;i<N;i++){
            retMatrix.setElement(i,matrix.getElement(i/n,i%n)-1,1);
        }
        return retMatrix;
    }

    private static boolean getStencil(int[][] matrix,int n,int m)throws Exception{
        Random r = new Random();
        int q = 0;
        int timer = 0;
        int N = n * m / 4;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        boolean isEnd = true;
        while (!list.isEmpty()) {
            boolean found = false;
            int val = -1;
            while (!found) {
                q = (q + 1) % 4;
                int index = r.nextInt(list.size());
                timer++;
                val = list.get(index);
                int[] arr = {};
                switch (q) {
                    case 0:
                        arr = getIndexesI(val,n,m);
                        break;
                    case 1:
                        arr = getIndexesII(val,n,m);
                        break;
                    case 2:
                        arr = getIndexesIII(val,n,m);
                        break;
                    case 3:
                        arr = getIndexesIV(val,n,m);
                        break;
                }
                if (timer == n * n * m * m) break;
                try {
                    if (matrix[arr[0] - 1][arr[1]] == 1) continue;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (matrix[arr[0] + 1][arr[1]] == 1) continue;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (matrix[arr[0]][arr[1] - 1] == 1) continue;
                } catch (IndexOutOfBoundsException ignored) {
                }
                try {
                    if (matrix[arr[0]][arr[1] + 1] == 1) continue;
                } catch (IndexOutOfBoundsException ignored) {
                }
                found = true;
                list.remove(index);
            }
            if (found) {
                int[] a = getIndexesI(val,n,m);
                int[] b = getIndexesII(val,n,m);
                int[] c = getIndexesIII(val,n,m);
                int[] d = getIndexesIV(val,n,m);
                if (q == 0) {
                    matrix[a[0]][a[1]] = 1;
                } else {
                    matrix[a[0]][a[1]] = 2;
                }
                if (q == 1) {
                    matrix[b[0]][b[1]] = 1;
                } else {
                    matrix[b[0]][b[1]] = 2;
                }
                if (q == 2) {
                    matrix[c[0]][c[1]] = 1;
                } else {
                    matrix[c[0]][c[1]] = 2;
                }
                if (q == 3) {
                    matrix[d[0]][d[1]] = 1;
                } else {
                    matrix[d[0]][d[1]] = 2;
                }
            } else {
                isEnd = false;
                break;
            }
        }
        return isEnd;
    }
    private static int[] getIndexesI(int val,int n,int m) {
        int _m = m/2;
        return new int[]{val / _m, val % _m};
    }

    private static int[] getIndexesII(int val,int n,int m) {
        int _m = m/2;
        return new int[]{val / _m, 2 * _m - val % _m - 1};
    }

    private static int[] getIndexesIII(int val,int n,int m) {
        int _n = n/2, _m = m/2;
        return new int[]{2 * _n - val / _m - 1, val % _m};
    }

    private static int[] getIndexesIV(int val,int n,int m) {
        int _n = n/2, _m = m/2;
        return new int[]{2 * _n - val / _m - 1, 2 * _m - val % _m - 1};
    }
}
