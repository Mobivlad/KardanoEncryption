package jcode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLAccessor {
    private Connection conn;
    private Statement st;
    public SQLAccessor() throws Exception{
        Class.forName("org.h2.Driver").newInstance();
        conn = DriverManager.getConnection("jdbc:h2:./database/Kardano", "sa", "pass");
        st = conn.createStatement();
    }

    public void finish() throws Exception{
       st.close();
       conn.close();
    }

    public void createTables(){
        try {
            st.execute("create table if not exists KardanoStencils(\n" +
                    "\tid int auto_increment,\n" +
                    "\ttitle VARCHAR(255) NOT NULL,\n" +
                    "\tn int not null,\n" +
                    "\tm int not null,\n" +
                    "\tPRIMARY KEY ( id ),\n" +
                    "\tUNIQUE ( title  )\n" +
                    ");");
        } catch (SQLException throwables) {
            try {
                st.execute("create table if not exists KardanoStencils(\n" +
                        "\tid int auto_increment,\n" +
                        "\ttitle VARCHAR(255) NOT NULL,\n" +
                        "\tn int not null,\n" +
                        "\tm int not null,\n" +
                        "\tPRIMARY KEY ( id ),\n" +
                        "\tUNIQUE ( title  )\n" +
                        ");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            st.execute("create table if not exists StencilData(\n" +
                    "\tstencilid int auto_increment not null,\n" +
                    "\ti int not null,\n" +
                    "\tj int not null,\n" +
                    "\tforeign key (stencilid) references KardanoStencils(id)\n" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Matrix readMatrix(String name){
        Matrix matrix = null;
        try {
            ResultSet x = st.executeQuery("Select * " +
                    "From KardanoStencils " +
                    "Where title=\'"+name+"\'");
            x.next();
            int id = x.getInt("id");
            int n = x.getInt("n");
            int m = x.getInt("m");
            matrix = new Matrix(n,m);
            ResultSet x1 = st.executeQuery("Select * " +
                    "From StencilData " +
                    "Where stencilid="+id);
            while(x1.next()){
                int i=x1.getInt("i");
                int j=x1.getInt("j");
                matrix.setElement(i,j,1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return matrix;
    }

    public void writeMatrix(Matrix matrix,String name){
        try {
            st.execute("insert into KardanoStencils(title,n,m) values (\'"+name+"\',"+matrix.n+","+matrix.m+")");
            ResultSet x = st.executeQuery("Select * " +
                    "From KardanoStencils " +
                    "Where title=\'"+name+"\'");
            if(x.next()){
                int id = x.getInt("id");
                int n = matrix.n;
                int m = matrix.m;
                for(int i=0;i<n;i++){
                    for(int j=0;j<m;j++){
                        if(matrix.getElement(i,j)==1)
                            st.execute(String.format("insert into stencildata(stencilid,i,j) values (%d,%d,%d);",id,i,j));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> readNames() throws Exception{
        List<String> list = new ArrayList<>();
        ResultSet x = st.executeQuery("Select * From KardanoStencils");
        while (x.next()){
            list.add(x.getString("title"));
        }
        return list;
    }

    public void removeMatrix(String name) throws Exception{
        ResultSet x = st.executeQuery("Select * " +
                "From KardanoStencils " +
                "Where title=\'"+name+"\'");
        x.next();
        int id = x.getInt("id");
        st.execute(String.format("delete from STENCILDATA where stencilid=%d",id));
        st.execute(String.format("delete from KARDANOSTENCILS  where id=%d",id));
    }

    public boolean exist(String name) throws Exception{
        ResultSet x = st.executeQuery("Select * " +
                "From KardanoStencils " +
                "Where title=\'"+name+"\'");
        return x.next();
    }
}
