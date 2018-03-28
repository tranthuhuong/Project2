package com.example.huongthutran.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView img;

    private TrucXanhBoard trucXanhBoard;
    TextView tv,tvAmount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imgView);
        tv=findViewById(R.id.tv);
        tvAmount=findViewById(R.id.tvAmount);
        trucXanhBoard = new TrucXanhBoard(MainActivity.this, 600,600,4,4);
        trucXanhBoard.init();
//        Line line = new Line(300,300, 3,3);
//        ChessBoard chessBoard = new ChessBoard(line);
        img.setImageBitmap(trucXanhBoard.drawBoard(img));
        tv.setText(trucXanhBoard.getTest());
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    trucXanhBoard.onTouch(view, motionEvent);
                    tvAmount.setText("Bạn còn "+(20-trucXanhBoard.getAmountPlay())+" lần lật");
                    return true;
                }
                return true;
            }
        });
    }
}
