package tests;

import jcode.AlgoFunc;
import jcode.Matrix;
import org.junit.Assert;
import org.junit.Test;

public class GeneratorTests {
    @Test
    public void generatorTest(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                try {
                    Matrix stencil = AlgoFunc.generateStencil(2*i,2*j);
                    Assert.assertTrue(AlgoFunc.checkStencil(stencil));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
