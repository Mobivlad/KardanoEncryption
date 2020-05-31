package tests;

import jcode.AlgoFunc;
import jcode.DecryptModel;
import jcode.EncryptModel;
import jcode.Matrix;
import org.junit.Assert;
import org.junit.Test;

public class LogicTest {
    @Test
    public void textLenEqualsStencilLen(){
        try {
            Matrix stencil = AlgoFunc.generateStencil(4,8);
            String in = "Abcdefghijklmnopqrstuvwxyzabcdef";
            String out = new DecryptModel().getAlgoRes(new EncryptModel().getAlgoRes(in,stencil,true,false),stencil,true,false);
            Assert.assertEquals(in,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void textLenNotEqualsStencilLen(){
        try {
            Matrix stencil = AlgoFunc.generateStencil(4,8);
            String in = "Abcdefghijklmnopqrstuv";
            String out = new DecryptModel().getAlgoRes(new EncryptModel().getAlgoRes(in,stencil,true,false),stencil,true,false);
            Assert.assertFalse(out.indexOf(in)==-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void textLenMultipleStencilLen(){
        try {
            Matrix stencil = AlgoFunc.generateStencil(4,8);
            String in = "AbcdefghijklmnopqrstuvwxyzabcdeffedcbazyxwvutsrqponmlkjihgfedcbA";
            String out = new DecryptModel().getAlgoRes(new EncryptModel().getAlgoRes(in,stencil,true,false),stencil,true,false);
            Assert.assertEquals(in,out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
