package jcode;

public abstract class AlgoModel {
    abstract String getAlgoRes(String s,Matrix stencil,boolean vertical,boolean useException) throws AlgorithmException;
    protected final String toString(Matrix m,int len){
        int _n=m.n;
        int _m=m.m;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<_n;i++){
            for(int j=0;j<_m;j++){
                sb.append((char) m.getElement(i,j));
            }
        }
        return sb.toString();
    }

    protected final Matrix toMatrix(String s,int len){
        int m = len;
        int n;
        if(s.length()%m==0){
            n = s.length()/m;
        } else {
            n = s.length()/m+1;
        }
        Matrix res = new Matrix(n,m);
        for(int i=0;i<s.length();i++){
            res.setElement(i/m,i%m,s.charAt(i));
        }
        if(s.length()%m!=0){
            for(int i=s.length()%m;i<m;i++){
                res.setElement(n-1,i,(int)'@');
            }
        }
        return res;
    }
}
