package com.example.huongthutran.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Huong Thu Tran on 3/28/2018.
 */

public class TrucXanhBoard {
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private int[][] board; //lưu id các hình
    private Context context;
    private int bitmapWidth, bitmapHeight, colQty,rowQty;
    private List<Line> lines;
    private Bitmap nomalBitmap,spriteMap;
    private String test="";
    private int[][] checkBoard; //lưu kết quả
    private int temp;
    //-1 khi chưa đc lật, 0 đang lật, 1 lật và đúng
    int amountPlay;
    int touch=0;


    public TrucXanhBoard(Context context, int bitmapWidth, int bitmapHeight, int colQty, int rowQty) {
        this.context = context;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.colQty = colQty;
        this.rowQty = rowQty;
    }
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public int getAmountPlay() {
        return amountPlay;
    }

    public void setAmountPlay(int amountPlay) {
        this.amountPlay = amountPlay;
    }

    //lam cac thao tac khoi tao, reset lại giá trị của các phương thức
    public void init(){
        lines = new ArrayList<>();
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        board = new int[rowQty][colQty];
        checkBoard = new int[rowQty][colQty];
        amountPlay=0;
        temp=-1;
        // lấy thứ tự hình
        random();

        paint.setStrokeWidth(2);
        int celWidth = bitmapWidth/colQty;
        int celHeight = bitmapHeight/rowQty;

        for(int i = 0; i < colQty; i++){
            for(int j = 0; j < rowQty; j++){
                checkBoard[i][j]=-1;
            }
        }
        for(int i = 0; i <= colQty; i++){
            lines.add(new Line(celWidth*i, 0, celWidth*i, bitmapHeight));
        }
        for(int i = 0; i <= rowQty; i++){
            lines.add(new Line(0, i*celHeight, bitmapWidth, i*celHeight));
        }
    }
    public Bitmap drawBoard(final View view){
        for(int i = 0; i < lines.size(); i++){
            canvas.drawLine(
                    lines.get(i).getX1(),
                    lines.get(i).getY1(),
                    lines.get(i).getX2(),
                    lines.get(i).getY2(),
                    paint
            );
        }

        spriteMap=BitmapFactory.decodeResource(context.getResources(),R.drawable.spritemap8);
        nomalBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.nomal);
        int cellWidth = view.getWidth() / colQty;
        int cellHeight = view.getHeight() / rowQty;

        for (int i=0;i<rowQty;i++){
            for(int j=0;j<colQty;j++){
                test+=board[i][j]+"    ";
                onDrawBoard(i,j,view,board[i][j],0);
                //truyền vào 0 nếu hiển thị nomal, 1 nếu hiển thị hình tương ứng vị trí
            }
        }
        return bitmap;
    }
    public void onDrawBoard(int rowIndex, int colIndex,final View view,int hinh,int i){
        int cellWidth = 600/4;
        int cellHeight = 600/4;
        int padding = 0;
        if(i==0){
            canvas.drawBitmap(
                    nomalBitmap,
                    new Rect(0,0,nomalBitmap.getWidth(), nomalBitmap.getHeight()),
                    new Rect(colIndex*cellWidth+padding,rowIndex*cellHeight+padding,(colIndex+1)*cellWidth -padding, (rowIndex+1)*cellHeight -padding),
                    paint);

        } else{
            Bitmap cropedBitmap = Bitmap.createBitmap(spriteMap, 0, (spriteMap.getHeight()/8)*hinh,spriteMap.getWidth() , spriteMap.getHeight()/8);
            canvas.drawBitmap(

                    cropedBitmap,
                    new Rect(0,0,cropedBitmap.getWidth(), cropedBitmap.getHeight()),
                    new Rect(colIndex*cellWidth+padding,rowIndex*cellHeight+padding,(colIndex+1)*cellWidth -padding, (rowIndex+1)*cellHeight -padding),
                    paint);
            view.invalidate();
        }


    }
    public boolean onTouch(final View view, MotionEvent motionEvent){
        int cellWidth = view.getWidth()/colQty;;
        int cellHeight = view.getWidth()/rowQty;;
        int colIndex = (int) (motionEvent.getX() / cellWidth);
        int rowIndex = (int) (motionEvent.getY() / cellHeight -1);



        if(checkBoard[rowIndex][colIndex]==0){
        }
        if(checkBoard[rowIndex][colIndex]==1){

        }

        if(checkBoard[rowIndex][colIndex]==-1) {
            checkBoard[rowIndex][colIndex] = 0;
            onDrawBoard(rowIndex, colIndex, view, board[rowIndex][colIndex], 1);
            view.invalidate();
            if (temp == -1) {
                temp = board[rowIndex][colIndex];//gán giá trị hình cho thằng temp
            } else {
                if (temp != board[rowIndex][colIndex]) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < rowQty; i++) {
                        for (int j = 0; j < colQty; j++) {

                            if (checkBoard[i][j] == 0) {
                                onDrawBoard(i, j, view, board[i][j], 0);
                                checkBoard[i][j] = -1;
                                view.invalidate();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "chính xác", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < rowQty; i++) {
                        for (int j = 0; j < colQty; j++) {
                            if (checkBoard[i][j] == 0) {
                                checkBoard[i][j] = 1;
                            }
                        }
                    }
                    if (checkWin()) {
                        Toast.makeText(context, "Win", Toast.LENGTH_SHORT).show();
                    }


                }

                amountPlay++;
                temp = -1;

            }
        }

        return true;
    }
    public boolean checkTrue(int a,int b){
        if(a==b){

            return true;
        }
        Toast.makeText(context,"Không chính xác",Toast.LENGTH_SHORT).show();
        return false;
    }
    public boolean checkWin(){
        if(amountPlay<20){
            for(int i=0;i<rowQty;i++){
                for (int j=0;j<colQty;j++){
                    if(checkBoard[i][j]!=1){
                        return false;

                    }
                }
            }
            Toast.makeText(context,"Thắng rồi",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    public void random(){
        int p[] = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};

        for (int i=15; i>0; --i)
        {
            //get swap index
            Random rand = new Random();
            int j = rand.nextInt(7)%i;
            //swap p[i] with p[j]
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        //copy first n elements from p to arr
        int countP=0;
        for (int i=0; i<rowQty; ++i)
            for (int j=0;j<colQty;++j){
                board[i][j] = p[countP];
                countP++;
            }

    }
}
