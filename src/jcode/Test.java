package jcode;

public class Test {
    public static void main(String[] args) throws Exception{
        Matrix x = AlgoFunc.generateStencil(8, 8);
        String s = "abcdefghklmnoprsk";
        String s1 = (new EncryptModel()).getAlgoRes(s,x,true);
        System.out.println(s1);
        String s2 = (new DecryptModel()).getAlgoRes(s1,x,true);
        System.out.println(s2);
    }
}
