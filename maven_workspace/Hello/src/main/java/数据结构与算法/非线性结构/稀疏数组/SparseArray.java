package 数据结构与算法.非线性结构.稀疏数组;
/**
 * 类介绍:介绍一般二维数组如何转化为稀疏数组<br/>
 *           稀疏数组如何还原为二维数组
 * */
public class SparseArray {
    /**
     * 方法介绍:生成一个原始数组
     * */
    public static int[][] generateSourceArr(){
        int[][] arr=new int[11][11];//创建一个原始的二维数组11*11
        arr[1][2]=1; //0:没有棋子   1:黑子    2:篮子
        arr[2][3]=2;
        for(int[] row:arr){
            for(int x:row){
                System.out.print(x+",");
            }
            System.out.println();
        }
        return arr;
    }
    /**
     * 方法介绍:把原始二维数组转化为 稀疏数组.<br/>
     * */
    public static int[][] sourceArrConvToSparseArr(int[][] sourceArr){
        //step1:输出原始数组的行,列和非初始值的个数.
        int src_row=sourceArr.length;//行数
        int src_col=sourceArr[0].length;//列数
        System.out.println("原始数组是:"+src_row+"*"+src_col);
        //输出非初始值个数有多少:
        int sum=0;//sum变量累计非初始值的个数
        for(int row=0;row<sourceArr.length;row++){
            for(int col=0;col< sourceArr[0].length;col++){
                if(sourceArr[row][col]!=0){
                    sum+=1;
                }
            }
        }
        // 初始化 稀疏数组 (sum+1) 行,3列
        int[][] sparseArr=new int[sum+1][3];
        sparseArr[0][0]=src_row;
        sparseArr[0][1]=src_col;
        sparseArr[0][2]=sum;
        //对非初始化的值赋值给稀疏数组
        int sparseArrRow=0;
        //遍历原始数组开始 给稀疏数组赋值
        for(int row=0;row<sourceArr.length;row++){
            for(int col=0;col< sourceArr[0].length;col++){
                if(sourceArr[row][col]!=0){
                    sparseArrRow+=1;
                    sparseArr[sparseArrRow][0]=row;
                    sparseArr[sparseArrRow][1]=col;
                    sparseArr[sparseArrRow][2]=sourceArr[row][col];
                }
            }
        }
        //遍历稀疏数组
        System.out.println("转换成稀疏数组是:");
        for(int[] x:sparseArr){
            for(int y:x){
                System.out.print(y+",");
            }
            System.out.println();
        }
        return sparseArr;
    }
    /**
     * 方法介绍:将稀疏数组还原为原始数组
     * */
    public static int[][] sparseArrConvToSourArr(int[][] sparseArr){
        //读取稀疏数组sparseArr[0][0],sparseArr[0][1] 用于初始化原始数组
        int sourceRow=sparseArr[0][0];
        int sourceCol=sparseArr[0][1];
        int[][] sourceArr=new int[sourceRow][sourceCol];
        for(int line=1;line< sparseArr.length;line++)
        {
            int x=sparseArr[line][0];
            int y=sparseArr[line][1];
            int val=sparseArr[line][2];
            sourceArr[x][y]=val;
        }
        for(int[] k:sourceArr)
        {
            for(int s:k){
                System.out.print(s+",");
            }
            System.out.println();
        }
        return sourceArr;
    }

    public static void main(String[] args) {
        int[][] sparseArr=sourceArrConvToSparseArr(generateSourceArr());
        sparseArrConvToSourArr(sparseArr);
    }
}
