package jcode;

public class DecryptModel extends AlgoModel{
    @Override
    public String getAlgoRes(String s, Matrix stencil,boolean vertical,boolean useException) throws AlgorithmException{
        return decrypt(s,stencil,vertical,useException);
    }

    private String decrypt(String s, Matrix stencil,boolean vertical,boolean useException) throws AlgorithmException{
        if(s.length()%(stencil.n*stencil.m)!=0 && useException){
            throw new AlgorithmException();
        }
        Matrix _matrix;
        if(vertical){
            _matrix = AlgoFunc.fillMatrixByRotating(stencil,true);
        } else {
            _matrix = AlgoFunc.fillMatrixByRotating(stencil,false);
        }
        _matrix.print(false);
        System.out.println();
        Matrix tmp = AlgoFunc.getDecryptMatrix(_matrix);
        tmp.print(false);
        System.out.println();
        Matrix res = toMatrix(s,tmp.n);
        res.print(false);
        System.out.println();
        res = Matrix.mul(res,tmp);
        return toString(res,tmp.n);
    }
}
