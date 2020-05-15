package jcode;

public class DecryptModel extends AlgoModel{
    @Override
    public String getAlgoRes(String s, Matrix stencil,boolean vertical){
        return decrypt(s,stencil,vertical);
    }

    private String decrypt(String s, Matrix stencil,boolean vertical){
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
