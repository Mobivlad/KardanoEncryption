package jcode;

public class EncryptModel extends AlgoModel{
    @Override
    public String getAlgoRes(String s, Matrix stencil,boolean vertical){
        return encrypt(s,stencil,vertical);
    }

    private String encrypt(String s, Matrix stencil,boolean vertical){
        Matrix _matrix;
        if(vertical){
            _matrix = AlgoFunc.fillMatrixByRotating(stencil,true);
        } else {
            _matrix = AlgoFunc.fillMatrixByRotating(stencil,false);
        }
        if(_matrix==null)return null;
        _matrix.print(false);
        System.out.println();
        Matrix tmp = AlgoFunc.getEncryptMatrix(_matrix);
        tmp.print(false);
        System.out.println();
        Matrix res = toMatrix(s,tmp.n);
        res.print(false);
        System.out.println();
        res = Matrix.mul(res,tmp);
        return toString(res,tmp.n);
    }
}
